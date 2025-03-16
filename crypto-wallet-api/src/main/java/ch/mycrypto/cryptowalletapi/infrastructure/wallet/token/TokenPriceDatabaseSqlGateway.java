package ch.mycrypto.cryptowalletapi.infrastructure.wallet.token;

import ch.mycrypto.cryptowalletapi.domain.exceptions.AssetIdNotFoundException;
import ch.mycrypto.cryptowalletapi.domain.exceptions.TokenPriceNotFoundException;
import ch.mycrypto.cryptowalletapi.domain.wallet.token.TokenInfoGateway;
import ch.mycrypto.cryptowalletapi.domain.wallet.token.TokenPrice;
import ch.mycrypto.cryptowalletapi.domain.wallet.token.TokenPriceGateway;
import ch.mycrypto.cryptowalletapi.infrastructure.mapper.wallet.TokenPriceMapper;
import ch.mycrypto.cryptowalletapi.infrastructure.wallet.token.model.TokenPriceHistoryResponse;
import ch.mycrypto.cryptowalletapi.infrastructure.wallet.token.model.TokenPriceResponse;
import ch.mycrypto.cryptowalletapi.infrastructure.wallet.token.persistence.TokenInfoEntity;
import ch.mycrypto.cryptowalletapi.infrastructure.wallet.token.persistence.TokenInfoRepository;
import ch.mycrypto.cryptowalletapi.infrastructure.wallet.token.persistence.TokenPriceRepository;
import ch.mycrypto.cryptowalletapi.infrastructure.wallet.token.restclient.TokenPriceRestClient;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneOffset;
import java.time.temporal.ChronoUnit;

@Repository
@RequiredArgsConstructor
@Log4j2
public class TokenPriceDatabaseSqlGateway implements TokenPriceGateway {

    private final TokenPriceRepository tokenPriceRepository;
    private final TokenPriceRestClient tokenPriceRestClient;
    private final TokenInfoRepository tokenInfoRepository;
    private final TokenPriceMapper tokenPriceMapper;
    private final TokenInfoGateway tokenInfoGateway;


    @Override
    public void save(TokenPrice tokenPrice) {
        tokenPriceRepository.save(tokenPriceMapper.fromTokenPrice(tokenPrice));
    }


    @Override
    public BigDecimal fetchPriceBySymbol(String symbol) {
        TokenInfoEntity tokenInfo = tokenInfoRepository.findBySymbolIgnoreCase(symbol)
                .orElseThrow(() -> new TokenPriceNotFoundException(symbol));
        TokenPriceResponse response = tokenPriceRestClient.fetchPriceByAssetId(tokenInfo.getAssetId());
        return new BigDecimal(response.priceUsd());
    }

    @Override
    public BigDecimal fetchPriceAt(String symbol, LocalDate date) {
        String assetId = findAssetIdBySymbol(symbol);

        Instant start = date.atStartOfDay(ZoneOffset.UTC).toInstant();
        Instant end = start.plus(1, ChronoUnit.DAYS);

        log.info("Calling fetchPriceHistory with assetId={}, start={}, end={}", assetId, start, end);

        TokenPriceHistoryResponse history = tokenPriceRestClient.fetchPriceHistory(assetId, start, end);

        return history.getFirstPrice()
                .orElseThrow(() -> new TokenPriceNotFoundException(symbol));
    }

    @Override
    public String findAssetIdBySymbol(String symbol) {
        return tokenInfoGateway.findAssetIdBySymbol(symbol)
                .orElseThrow(() -> new AssetIdNotFoundException(symbol));
    }

}
