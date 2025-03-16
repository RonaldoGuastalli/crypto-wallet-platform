package ch.mycrypto.cryptowalletapi.domain.wallet.asset;

import ch.mycrypto.cryptowalletapi.domain.validation.Error;
import ch.mycrypto.cryptowalletapi.domain.validation.ValidationHandler;
import ch.mycrypto.cryptowalletapi.domain.validation.handler.ThrowsValidationHandler;

import java.math.BigDecimal;


public class Asset {
    private String symbol;
    private BigDecimal quantity;
    private BigDecimal price;

    private Asset(
            String symbol,
            BigDecimal quantity,
            BigDecimal price
    ) {
        this.symbol = symbol;
        this.quantity = quantity;
        this.price = price;
        validate(new ThrowsValidationHandler());
    }

    public static Asset with(
            final String symbol,
            final BigDecimal quantity,
            final BigDecimal price
    ) {
        return new Asset(symbol, quantity, price);
    }

    public static Asset with(final Asset anAsset) {
        return with(
                anAsset.symbol(),
                anAsset.quantity(),
                anAsset.price()
        );
    }


    public void validate(final ValidationHandler handler) {
        if (symbol == null || symbol.isBlank()) {
            handler.append(new Error("'symbol' must not be null or blank"));
        }
        if (quantity == null || quantity.compareTo(BigDecimal.ZERO) <= 0) {
            handler.append(new Error("'quantity' must be greater than zero"));
        }
        if (price == null || price.compareTo(BigDecimal.ZERO) <= 0) {
            handler.append(new Error("'price' must be greater than zero"));
        }
    }

    public BigDecimal getValue() {
        return price.multiply(quantity);
    }

    public Asset increaseQuantity(BigDecimal extra) {
        return Asset.with(this.symbol, this.quantity.add(extra), this.price);
    }

    public Asset updatePrice(BigDecimal newPrice) {
        this.price = newPrice;
        return this;
    }

    public String symbol() {
        return symbol;
    }

    public BigDecimal quantity() {
        return quantity;
    }

    public BigDecimal price() {
        return price;
    }
}
