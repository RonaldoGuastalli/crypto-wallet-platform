package ch.mycrypto.cryptowalletapi.infrastructure.wallet.model;


import com.fasterxml.jackson.annotation.JsonInclude;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@JsonInclude(JsonInclude.Include.NON_NULL)
public record WalletEvaluationHttpRequest(
        List<TokenAssetRequest> assets,
        LocalDate date
) {
    public record TokenAssetRequest(String symbol, BigDecimal quantity, BigDecimal value) {
    }
}
