package ch.mycrypto.cryptowalletapi.domain.wallet.token;

import java.time.LocalDate;

public class TokenEvaluationDate {

    private final LocalDate value;

    private TokenEvaluationDate(LocalDate value) {
        if (value.isAfter(LocalDate.now())) {
            throw new IllegalArgumentException("Evaluation date cannot be in the future.");
        }
        this.value = value;
    }

    public static TokenEvaluationDate of(LocalDate value) {
        return new TokenEvaluationDate(value);
    }

    public LocalDate value() {
        return value;
    }
}
