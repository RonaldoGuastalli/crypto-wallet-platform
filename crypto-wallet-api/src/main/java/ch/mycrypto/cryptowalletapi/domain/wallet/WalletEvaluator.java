package ch.mycrypto.cryptowalletapi.domain.wallet;

import ch.mycrypto.cryptowalletapi.domain.wallet.token.TokenEvaluation;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class WalletEvaluator {

    public static WalletEvaluationResult evaluate(List<TokenEvaluation> tokens) {
        BigDecimal total = tokens.stream()
                .map(TokenEvaluation::valueNow)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        TokenEvaluation best = Collections.max(tokens, Comparator.comparing(TokenEvaluation::performancePercentage));
        TokenEvaluation worst = Collections.min(tokens, Comparator.comparing(TokenEvaluation::performancePercentage));

        return new WalletEvaluationResult(
                total,
                best.symbol(),
                best.performancePercentage(),
                worst.symbol(),
                worst.performancePercentage()
        );
    }
}


