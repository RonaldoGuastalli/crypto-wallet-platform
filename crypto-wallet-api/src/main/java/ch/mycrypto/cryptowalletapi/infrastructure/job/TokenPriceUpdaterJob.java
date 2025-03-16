package ch.mycrypto.cryptowalletapi.infrastructure.job;

import ch.mycrypto.cryptowalletapi.domain.wallet.WalletGateway;
import ch.mycrypto.cryptowalletapi.domain.wallet.token.TokenPrice;
import ch.mycrypto.cryptowalletapi.domain.wallet.token.TokenPriceGateway;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.concurrent.ExecutorService;

@Service
@RequiredArgsConstructor
public class TokenPriceUpdaterJob {

    private final WalletGateway walletGateway;
    private final TokenPriceGateway tokenPriceGateway;
    private final ExecutorService executor;

    @Scheduled(
            fixedRateString = "${job.token-price.fixed-rate-ms}",
            initialDelayString = "${job.token-price.initial-delay-ms}"
    )
    public void updateTokenPrices() {
        List<String> symbols = walletGateway.findAllUniqueSymbols();

        try {
            for (String symbol : symbols) {
                executor.submit(() -> {
                    try {
                        BigDecimal price = tokenPriceGateway.fetchPriceBySymbol(symbol);
                        TokenPrice tokenPrice = TokenPrice.with(symbol, price, LocalDateTime.now());
                        tokenPriceGateway.save(tokenPrice);
                    } catch (Exception e) {
                        //TODO: log here
                    }
                });
            }
        } finally {
            executor.shutdown();
        }
    }

}
