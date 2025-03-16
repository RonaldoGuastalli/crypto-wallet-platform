package ch.mycrypto.cryptowalletapi.infrastructure.mapper.wallet;

import ch.mycrypto.cryptowalletapi.domain.wallet.token.TokenPrice;
import ch.mycrypto.cryptowalletapi.infrastructure.wallet.token.model.TokenPriceDto;
import ch.mycrypto.cryptowalletapi.infrastructure.wallet.token.persistence.TokenPriceEntity;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface TokenPriceMapper {

    TokenPriceDto toDto(TokenPriceEntity entity);

    TokenPriceEntity toEntity(TokenPriceDto dto);

    default TokenPriceEntity fromTokenPrice(TokenPrice tokenPrice) {
        if (tokenPrice == null) return null;
        return TokenPriceEntity.builder()
                .symbol(tokenPrice.symbol())
                .price(tokenPrice.price())
                .timestamp(tokenPrice.timestamp())
                .build();
    }
}
