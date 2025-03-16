package ch.mycrypto.cryptowalletapi.application.wallet.get;

import ch.mycrypto.cryptowalletapi.application.UseCase;
import ch.mycrypto.cryptowalletapi.domain.exceptions.WalletNotFoundException;
import ch.mycrypto.cryptowalletapi.domain.wallet.Wallet;
import ch.mycrypto.cryptowalletapi.domain.wallet.WalletGateway;
import org.springframework.stereotype.Service;

@Service
public class GetWalletUseCase extends UseCase<GetWalletUseCase.Input, GetWalletUseCase.Output> {

    private final WalletGateway walletGateway;

    public GetWalletUseCase(WalletGateway walletGateway) {
        this.walletGateway = walletGateway;
    }

    @Override
    public Output execute(Input input) {
        Wallet wallet = walletGateway.findById(input.walletId)
                .orElseThrow(() -> new WalletNotFoundException(input.walletId));
        return new Output(wallet);
    }

    public record Input(String walletId) {
    }

    public record Output(Wallet wallet) {
    }
}

