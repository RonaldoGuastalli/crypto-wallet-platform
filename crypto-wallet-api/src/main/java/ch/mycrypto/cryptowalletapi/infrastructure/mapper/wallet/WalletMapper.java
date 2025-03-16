package ch.mycrypto.cryptowalletapi.infrastructure.mapper.wallet;

import ch.mycrypto.cryptowalletapi.infrastructure.wallet.asset.model.AssetDto;
import ch.mycrypto.cryptowalletapi.infrastructure.wallet.model.WalletDto;
import ch.mycrypto.cryptowalletapi.infrastructure.wallet.asset.persistence.AssetEntity;
import ch.mycrypto.cryptowalletapi.infrastructure.wallet.persistence.WalletEntity;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface WalletMapper {

    WalletDto toDto(WalletEntity entity);

    WalletEntity toEntity(WalletDto dto);

    List<AssetDto> toDto(List<AssetEntity> entities);

    List<AssetEntity> toEntity(List<AssetDto> dtos);

}