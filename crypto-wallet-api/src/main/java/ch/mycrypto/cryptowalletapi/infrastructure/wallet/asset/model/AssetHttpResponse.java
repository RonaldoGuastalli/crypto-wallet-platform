package ch.mycrypto.cryptowalletapi.infrastructure.wallet.asset.model;

import java.math.BigDecimal;

public record AssetHttpResponse(
        String symbol,
        BigDecimal quantity,
        BigDecimal price,
        BigDecimal value
) {}