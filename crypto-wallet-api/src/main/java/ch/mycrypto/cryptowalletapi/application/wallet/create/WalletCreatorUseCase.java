package ch.mycrypto.cryptowalletapi.application.wallet.create;

import ch.mycrypto.cryptowalletapi.application.UseCase;
import ch.mycrypto.cryptowalletapi.domain.exceptions.EmailAlreadyUsedException;
import ch.mycrypto.cryptowalletapi.domain.user.UserGateway;
import ch.mycrypto.cryptowalletapi.domain.wallet.WalletGateway;
import ch.mycrypto.cryptowalletapi.domain.user.User;
import ch.mycrypto.cryptowalletapi.domain.wallet.Wallet;
import ch.mycrypto.cryptowalletapi.domain.wallet.asset.Asset;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

@Service
public class WalletCreatorUseCase extends UseCase<WalletCreatorUseCase.Input, WalletCreatorUseCase.Output> {

    private final UserGateway userGateway;
    private final WalletGateway walletGateway;

    public WalletCreatorUseCase(
            UserGateway userGateway,
            WalletGateway walletGateway) {
        this.userGateway = userGateway;
        this.walletGateway = walletGateway;
    }

    @Override
    public Output execute(Input input) {
        userGateway.findByEmail(input.email)
                .ifPresent(user -> {
                    throw new EmailAlreadyUsedException(input.email);
                });

        final String userId = input.userId() != null ? input.userId() : UUID.randomUUID().toString();
        final User userToCreate = User.with(userId, input.email());
        final User createdUser = userGateway.save(userToCreate);

        final Wallet wallet = Wallet.with(UUID.randomUUID().toString(), createdUser.id(), new HashSet<>());
        walletGateway.save(wallet);

        return new Output(wallet.id(), wallet.getTotalValue(), wallet.assets());
    }


    public record Input(
            String userId,
            String email
    ) {
    }

    public record Output(
            String id,
            BigDecimal total,
            Set<Asset> assets
    ) {
    }
}
