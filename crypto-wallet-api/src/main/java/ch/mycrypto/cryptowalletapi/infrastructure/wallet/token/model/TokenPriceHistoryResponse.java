package ch.mycrypto.cryptowalletapi.infrastructure.wallet.token.model;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

public record TokenPriceHistoryResponse(List<PriceData> data) {

    public Optional<BigDecimal> getFirstPrice() {
        return data != null && !data.isEmpty()
                ? Optional.of(new BigDecimal(data.get(0).priceUsd()))
                : Optional.empty();
    }

    public record PriceData(String priceUsd, long time) {
    }
}
