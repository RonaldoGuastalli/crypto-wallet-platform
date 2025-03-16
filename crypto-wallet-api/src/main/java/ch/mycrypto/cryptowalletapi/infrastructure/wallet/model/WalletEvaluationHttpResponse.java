package ch.mycrypto.cryptowalletapi.infrastructure.wallet.model;

import java.math.BigDecimal;

public record WalletEvaluationHttpResponse(
        BigDecimal total,
        String bestAsset,
        BigDecimal bestPerformance,
        String worstAsset,
        BigDecimal worstPerformance
) {
}
