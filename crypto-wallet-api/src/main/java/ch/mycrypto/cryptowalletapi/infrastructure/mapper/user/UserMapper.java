package ch.mycrypto.cryptowalletapi.infrastructure.mapper.user;

import ch.mycrypto.cryptowalletapi.infrastructure.user.model.UserDto;
import ch.mycrypto.cryptowalletapi.infrastructure.user.persistence.UserEntity;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface UserMapper {

    UserDto toDto(UserEntity entity);

    UserEntity toEntity(UserDto dto);
}
