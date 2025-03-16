package ch.mycrypto.cryptowalletapi.infrastructure.user;

import ch.mycrypto.cryptowalletapi.domain.user.User;
import ch.mycrypto.cryptowalletapi.infrastructure.user.model.UserDto;
import ch.mycrypto.cryptowalletapi.infrastructure.user.persistence.UserEntity;
import ch.mycrypto.cryptowalletapi.infrastructure.mapper.user.UserMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class UserFactory {

    private final UserMapper mapper;

    public User fromUserEntity(UserEntity entity) {
        return User.with(
                entity.id().toString(),
                entity.email());
    }

    public UserEntity toEntity(User user) {
        return mapper.toEntity(toDto(user));
    }

    public UserDto toDto(User user) {
        return new UserDto(user.id(), user.email());
    }
}
