package ch.mycrypto.cryptowalletapi.domain.wallet.token;

import ch.mycrypto.cryptowalletapi.domain.validation.Error;
import ch.mycrypto.cryptowalletapi.domain.validation.ValidationHandler;
import ch.mycrypto.cryptowalletapi.domain.validation.handler.ThrowsValidationHandler;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class TokenPrice {
    String symbol;
    BigDecimal price;
    LocalDateTime timestamp;

    private TokenPrice(
            String symbol,
            BigDecimal price,
            LocalDateTime timestamp) {
        this.symbol = symbol;
        this.price = price;
        this.timestamp = timestamp;
        validate(new ThrowsValidationHandler());
    }

    public static TokenPrice with(
            final String symbol,
            final BigDecimal price,
            final LocalDateTime timestamp
    ) {
        return new TokenPrice(symbol, price, timestamp);
    }

    public static TokenPrice with(final TokenPrice aTokenPrice) {
        return with(
                aTokenPrice.symbol(),
                aTokenPrice.price(),
                aTokenPrice.timestamp()
        );
    }

    public void validate(final ValidationHandler handler) {
        if (symbol == null || symbol.isBlank()) {
            handler.append(new Error("'symbol' must not be null or blank"));
        }

        if (price == null || price.compareTo(BigDecimal.ZERO) <= 0) {
            handler.append(new Error("price' must be greater than zero"));
        }

        if (timestamp == null) {
            throw new IllegalArgumentException("'timestamp' must not be null");
        }
    }


    public String symbol() {
        return symbol;
    }

    public BigDecimal price() {
        return price;
    }

    public LocalDateTime timestamp() {
        return timestamp;
    }
}
