package ch.mycrypto.cryptowalletapi.domain.wallet;

import java.math.BigDecimal;

public record WalletEvaluationResult(
        BigDecimal total,
        String bestAsset,
        BigDecimal bestPerformance,
        String worstAsset,
        BigDecimal worstPerformance
) {
}
