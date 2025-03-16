package ch.mycrypto.cryptowalletapi.infrastructure.wallet;

import ch.mycrypto.cryptowalletapi.domain.wallet.Wallet;
import ch.mycrypto.cryptowalletapi.domain.wallet.asset.Asset;
import ch.mycrypto.cryptowalletapi.infrastructure.mapper.wallet.WalletMapper;
import ch.mycrypto.cryptowalletapi.infrastructure.wallet.asset.model.AssetDto;
import ch.mycrypto.cryptowalletapi.infrastructure.wallet.asset.persistence.AssetEntity;
import ch.mycrypto.cryptowalletapi.infrastructure.wallet.asset.persistence.AssetFactory;
import ch.mycrypto.cryptowalletapi.infrastructure.wallet.model.WalletDto;
import ch.mycrypto.cryptowalletapi.infrastructure.wallet.persistence.WalletEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class WalletFactory {

    private final WalletMapper mapper;
    private final AssetFactory assetFactory;

    public Wallet fromDto(WalletDto dto) {
        Set<Asset> domainAssets = dto.assets().stream()
                .map(assetFactory::fromDto)
                .collect(Collectors.toSet());

        return Wallet.with(dto.id(), dto.userId(), domainAssets);
    }

    public WalletDto toDto(Wallet wallet) {

        List<AssetDto> dtoAssets = wallet.assets().stream()
                .map(assetFactory::toDto)
                .toList();
        return new WalletDto(wallet.id(), wallet.userId(), dtoAssets);
    }

    public WalletEntity toEntity(WalletDto dto) {
        WalletEntity wallet = mapper.toEntity(dto);
        wallet.setId(UUID.fromString(dto.id()));

        List<AssetEntity> assets = dto.assets().stream()
                .map(assetFactory::toEntity)
                .peek(asset -> asset.setWallet(wallet))
                .toList();

        wallet.setAssets(assets);
        return wallet;
    }

    public WalletDto toDto(WalletEntity entity) {
        return mapper.toDto(entity);
    }
}
