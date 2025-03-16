package ch.mycrypto.cryptowalletapi.unit;

import ch.mycrypto.cryptowalletapi.config.TestExecutorConfig;
import ch.mycrypto.cryptowalletapi.domain.exceptions.TokenPriceNotFoundException;
import ch.mycrypto.cryptowalletapi.domain.wallet.WalletGateway;
import ch.mycrypto.cryptowalletapi.domain.wallet.token.TokenPriceGateway;
import ch.mycrypto.cryptowalletapi.infrastructure.job.TokenPriceUpdaterJob;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.context.annotation.Import;
import org.springframework.web.client.RestClient;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static org.awaitility.Awaitility.await;
import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.Mockito.*;

@Import(TestExecutorConfig.class)
@ExtendWith(MockitoExtension.class)
class TokenPriceUpdaterJobUnitTest {

    @Mock
    private WalletGateway walletGateway;
    @Mock
    private TokenPriceGateway tokenPriceGateway;
    @Mock
    private RestClient restClient;

    private TokenPriceUpdaterJob service;

    @BeforeEach
    void setup() {
        ExecutorService synchronousExecutor = Executors.newSingleThreadExecutor();
        service = new TokenPriceUpdaterJob(walletGateway, tokenPriceGateway, synchronousExecutor);
    }

    @Test
    @DisplayName("Should update token prices for all unique symbols")
    void testUpdateTokenPrices_whenSymbolsExist_shouldFetchAndSavePrices() {
        // given
        List<String> symbols = List.of("BTC", "ETH", "ADA");
        when(walletGateway.findAllUniqueSymbols()).thenReturn(symbols);

        for (String symbol : symbols) {
            when(tokenPriceGateway.fetchPriceBySymbol(symbol)).thenReturn(new BigDecimal("1000"));
        }

        // when
        service.updateTokenPrices();

        // then: wait for ExecutorService to finish
        await().untilAsserted(() -> {
            for (String symbol : symbols) {
                verify(tokenPriceGateway).fetchPriceBySymbol(symbol);
                verify(tokenPriceGateway).save(argThat(price ->
                        price.symbol().equals(symbol)
                                && price.price().compareTo(new BigDecimal("1000")) == 0
                ));
            }
        });
    }

    @Test
    @DisplayName("Should handle empty symbol list gracefully")
    void testUpdateTokenPrices_whenNoSymbols_shouldDoNothing() {
        // given
        when(walletGateway.findAllUniqueSymbols()).thenReturn(Collections.emptyList());

        // when
        service.updateTokenPrices();

        // then
        verifyNoInteractions(tokenPriceGateway);
    }

    @Test
    @DisplayName("Should handle exception during price fetch without crashing")
    void testUpdateTokenPrices_whenClientFails_shouldSkipAndContinue() {
        // given
        when(walletGateway.findAllUniqueSymbols()).thenReturn(List.of("BTC", "FAIL", "ETH"));
        when(tokenPriceGateway.fetchPriceBySymbol("BTC")).thenReturn(new BigDecimal("1000"));
        when(tokenPriceGateway.fetchPriceBySymbol("FAIL")).thenThrow(new TokenPriceNotFoundException("Failed"));
        when(tokenPriceGateway.fetchPriceBySymbol("ETH")).thenReturn(new BigDecimal("1600"));

        // when
        service.updateTokenPrices();

        // then
        verify(tokenPriceGateway).fetchPriceBySymbol("BTC");
        verify(tokenPriceGateway).fetchPriceBySymbol("FAIL");
        verify(tokenPriceGateway).fetchPriceBySymbol("ETH");

        // BTC and ETH only
        verify(tokenPriceGateway, times(2)).save(any());
    }
}
