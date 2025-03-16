package ch.mycrypto.cryptowalletapi.application.wallet.addAsset;

import ch.mycrypto.cryptowalletapi.application.UseCase;
import ch.mycrypto.cryptowalletapi.domain.exceptions.DomainException;
import ch.mycrypto.cryptowalletapi.domain.validation.Error;
import ch.mycrypto.cryptowalletapi.domain.wallet.Wallet;
import ch.mycrypto.cryptowalletapi.domain.wallet.WalletGateway;
import ch.mycrypto.cryptowalletapi.domain.wallet.asset.Asset;
import ch.mycrypto.cryptowalletapi.domain.wallet.token.TokenPrice;
import ch.mycrypto.cryptowalletapi.domain.wallet.token.TokenPriceGateway;
import ch.mycrypto.cryptowalletapi.domain.exceptions.WalletNotFoundException;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Set;

@Service
public class AddAssetToWalletUseCase extends UseCase<AddAssetToWalletUseCase.Input, AddAssetToWalletUseCase.Output> {

    private final WalletGateway walletGateway;
    private final TokenPriceGateway tokenPriceGateway;

    public AddAssetToWalletUseCase(WalletGateway walletGateway,
                                   TokenPriceGateway tokenPriceGateway
    ) {
        this.walletGateway = walletGateway;
        this.tokenPriceGateway = tokenPriceGateway;
    }

    @Override
    public Output execute(Input input) {
        if (input == null) {
            throw DomainException.with(new Error("'AddAssetToWalletUserCase.Input' cannot be null"));
        }

        Wallet wallet = walletGateway.findById(input.walletId())
                .orElseThrow(() -> new WalletNotFoundException(input.walletId()));

        try {
            BigDecimal price = tokenPriceGateway.fetchPriceBySymbol(input.symbol());
            TokenPrice tokenPrice = TokenPrice.with(input.symbol(), price, LocalDateTime.now());
            tokenPriceGateway.save(tokenPrice);

            Asset asset = Asset.with(input.symbol(), input.quantity(), price);
            Wallet updatedWallet = wallet.addOrUpdateAsset(asset);

            walletGateway.save(updatedWallet);

            return new Output(updatedWallet.id(), wallet.getTotalValue(), wallet.assets());
        } catch (Exception exception) {
            String msgError = String.format(
                    "Error in added the asset symbol '%s'", input.symbol()
            );
            throw DomainException.with(new Error(msgError));
        }

    }

    public record Input(
            String walletId,
            String symbol,
            BigDecimal price,
            BigDecimal quantity
    ) {
    }

    public record Output(
            String id,
            BigDecimal total,
            Set<Asset> assets
    ) {
    }

}
