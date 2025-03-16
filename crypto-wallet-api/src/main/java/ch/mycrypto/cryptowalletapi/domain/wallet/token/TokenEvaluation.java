package ch.mycrypto.cryptowalletapi.domain.wallet.token;

import java.math.BigDecimal;
import java.math.RoundingMode;

public record TokenEvaluation(
        String symbol,
        BigDecimal quantity,
        BigDecimal priceAtDate,
        BigDecimal currentPrice
) {

    public BigDecimal valueNow() {
        return quantity.multiply(currentPrice);
    }

    public BigDecimal performancePercentage() {
        if (priceAtDate.compareTo(BigDecimal.ZERO) == 0) return BigDecimal.ZERO;
        return currentPrice.subtract(priceAtDate)
                .divide(priceAtDate, 4, RoundingMode.HALF_UP)
                .multiply(BigDecimal.valueOf(100));
    }
}
