package ch.mycrypto.cryptowalletapi.infrastructure.mapper.wallet;

import ch.mycrypto.cryptowalletapi.infrastructure.wallet.asset.model.AssetDto;
import ch.mycrypto.cryptowalletapi.infrastructure.wallet.asset.persistence.AssetEntity;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface AssetMapper {

    AssetDto toDto(AssetEntity entity);

    AssetEntity toEntity(AssetDto dto);
}
