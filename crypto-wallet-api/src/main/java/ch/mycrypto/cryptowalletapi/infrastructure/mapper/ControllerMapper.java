package ch.mycrypto.cryptowalletapi.infrastructure.mapper;

import ch.mycrypto.cryptowalletapi.application.wallet.addAsset.AddAssetToWalletUseCase;
import ch.mycrypto.cryptowalletapi.application.wallet.create.WalletCreatorUseCase;
import ch.mycrypto.cryptowalletapi.domain.wallet.asset.Asset;
import ch.mycrypto.cryptowalletapi.infrastructure.wallet.asset.model.AssetHttpResponse;
import ch.mycrypto.cryptowalletapi.infrastructure.wallet.model.WalletHttpResponse;

import java.util.Set;
import java.util.stream.Collectors;

public class ControllerMapper {

    public static Set<AssetHttpResponse> toResponse(Set<Asset> assets) {
        return assets.stream()
                .map(asset -> new AssetHttpResponse(
                        asset.symbol(),
                        asset.quantity(),
                        asset.price(),
                        asset.getValue()
                )).collect(Collectors.toSet());
    }


    public static AssetHttpResponse toResponse(Asset asset) {
        return new AssetHttpResponse(
                asset.symbol(),
                asset.quantity(),
                asset.price(),
                asset.getValue()
        );
    }

    public static WalletHttpResponse toResponse(AddAssetToWalletUseCase.Output output) {
        return new WalletHttpResponse(
                output.id(),
                output.total(),
                toResponse(output.assets())
        );
    }

    public static WalletHttpResponse toResponse(WalletCreatorUseCase.Output output) {
        return new WalletHttpResponse(
                output.id(),
                output.total(),
                toResponse(output.assets())
        );
    }
}
