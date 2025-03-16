package ch.mycrypto.cryptowalletapi.infrastructure.wallet.asset.persistence;

import ch.mycrypto.cryptowalletapi.domain.wallet.asset.Asset;
import ch.mycrypto.cryptowalletapi.infrastructure.mapper.wallet.AssetMapper;
import ch.mycrypto.cryptowalletapi.infrastructure.wallet.asset.model.AssetDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class AssetFactory {

    private final AssetMapper mapper;

    public Asset fromDto(AssetDto dto) {
        return Asset.with(dto.symbol(), dto.quantity(), dto.price());
    }

    public AssetDto toDto(Asset asset) {
        return new AssetDto(asset.symbol(), asset.quantity(), asset.price());
    }

    public AssetEntity toEntity(AssetDto dto) {
        AssetEntity entity = mapper.toEntity(dto);
        return entity;
    }

    public AssetDto toDto(AssetEntity entity) {
        return mapper.toDto(entity);
    }
}
