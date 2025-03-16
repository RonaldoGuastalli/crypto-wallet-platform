package ch.mycrypto.cryptowalletapi.domain.wallet.token;

import java.math.BigDecimal;
import java.time.LocalDate;

public interface TokenPriceGateway {

    void save(TokenPrice price);

    BigDecimal fetchPriceBySymbol(String symbol);

    BigDecimal fetchPriceAt(String symbol, LocalDate date);

    String findAssetIdBySymbol(String symbol);
}
