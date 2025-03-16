package ch.mycrypto.cryptowalletapi.infrastructure.wallet.model;

import ch.mycrypto.cryptowalletapi.infrastructure.wallet.asset.model.AssetHttpResponse;

import java.math.BigDecimal;
import java.util.Set;

public record WalletHttpResponse(
        String id,
        BigDecimal total,
        Set<AssetHttpResponse> assets
) {
}