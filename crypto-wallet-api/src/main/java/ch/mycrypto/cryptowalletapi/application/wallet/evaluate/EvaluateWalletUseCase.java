package ch.mycrypto.cryptowalletapi.application.wallet.evaluate;

import ch.mycrypto.cryptowalletapi.application.UseCase;
import ch.mycrypto.cryptowalletapi.domain.wallet.WalletEvaluationResult;
import ch.mycrypto.cryptowalletapi.domain.wallet.WalletEvaluator;
import ch.mycrypto.cryptowalletapi.domain.wallet.token.TokenEvaluation;
import ch.mycrypto.cryptowalletapi.domain.wallet.token.TokenEvaluationDate;
import ch.mycrypto.cryptowalletapi.domain.wallet.token.TokenPriceGateway;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Service
public class EvaluateWalletUseCase extends UseCase<EvaluateWalletUseCase.Input, EvaluateWalletUseCase.Output> {

    private final TokenPriceGateway tokenPriceGateway;

    public EvaluateWalletUseCase(TokenPriceGateway tokenPriceGateway) {
        this.tokenPriceGateway = tokenPriceGateway;
    }

    @Override
    public Output execute(Input input) {
        TokenEvaluationDate evaluationDate = TokenEvaluationDate.of(
                input.date() != null ? input.date() : LocalDate.now().minusDays(1)
        );

        List<TokenEvaluation> evaluations = input.assets().stream()
                .map(asset -> {
                    BigDecimal priceAtDate = tokenPriceGateway.fetchPriceAt(asset.symbol(), evaluationDate.value());
                    BigDecimal currentPrice = tokenPriceGateway.fetchPriceBySymbol(asset.symbol());

                    return new TokenEvaluation(asset.symbol(), asset.quantity(), priceAtDate, currentPrice);
                })
                .toList();

        WalletEvaluationResult result = WalletEvaluator.evaluate(evaluations);

        return new Output(
                result.total(),
                result.bestAsset(),
                result.bestPerformance(),
                result.worstAsset(),
                result.worstPerformance()
        );
    }


    public record Input(
            List<TokenAsset> assets,
            LocalDate date
    ) {
        public record TokenAsset(String symbol, BigDecimal quantity, BigDecimal value) {
        }
    }

    public record Output(
            BigDecimal total,
            String bestAsset,
            BigDecimal bestPerformance,
            String worstAsset,
            BigDecimal worstPerformance
    ) {
    }
}


