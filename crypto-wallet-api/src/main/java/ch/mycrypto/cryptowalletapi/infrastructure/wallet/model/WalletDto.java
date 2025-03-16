package ch.mycrypto.cryptowalletapi.infrastructure.wallet.model;

import ch.mycrypto.cryptowalletapi.infrastructure.wallet.asset.model.AssetDto;

import java.util.List;

public record WalletDto(
        String id,
        String userId,
        List<AssetDto> assets
) {
}
