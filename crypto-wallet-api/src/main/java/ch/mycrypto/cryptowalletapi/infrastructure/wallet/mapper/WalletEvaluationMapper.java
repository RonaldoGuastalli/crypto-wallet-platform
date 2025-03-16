package ch.mycrypto.cryptowalletapi.infrastructure.wallet.mapper;

import ch.mycrypto.cryptowalletapi.application.wallet.evaluate.EvaluateWalletUseCase;
import ch.mycrypto.cryptowalletapi.infrastructure.wallet.model.WalletEvaluationHttpRequest;
import ch.mycrypto.cryptowalletapi.infrastructure.wallet.model.WalletEvaluationHttpResponse;
import org.springframework.stereotype.Component;

@Component
public class WalletEvaluationMapper {

    public EvaluateWalletUseCase.Input toInput(WalletEvaluationHttpRequest request) {
        return new EvaluateWalletUseCase.Input(
                request.assets().stream()
                        .map(a -> new EvaluateWalletUseCase.Input.TokenAsset(
                                a.symbol(),
                                a.quantity(),
                                a.value())
                        )
                        .toList(),
                request.date()
        );
    }

    public WalletEvaluationHttpResponse toResponse(EvaluateWalletUseCase.Output output) {
        return new WalletEvaluationHttpResponse(
                output.total(),
                output.bestAsset(),
                output.bestPerformance(),
                output.worstAsset(),
                output.worstPerformance()
        );
    }
}
