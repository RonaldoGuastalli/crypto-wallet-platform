package ch.mycrypto.cryptowalletapi.infrastructure.user;

import ch.mycrypto.cryptowalletapi.domain.user.UserGateway;
import ch.mycrypto.cryptowalletapi.domain.user.User;
import ch.mycrypto.cryptowalletapi.infrastructure.user.persistence.UserEntity;
import ch.mycrypto.cryptowalletapi.infrastructure.mapper.user.UserMapper;
import ch.mycrypto.cryptowalletapi.infrastructure.user.persistence.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.Optional;


@Repository
@RequiredArgsConstructor
public class UserDatabaseSqlGateway implements UserGateway {

    private final UserRepository userRepository;
    private final UserFactory userFactory;
    private final UserMapper userMapper;


    @Override
    public User save(User user) {
        UserEntity entity = userMapper.toEntity(userFactory.toDto(user));
        UserEntity saved = userRepository.save(entity);
        return userFactory.fromUserEntity(saved);
    }

    @Override
    public Optional<User> findByEmail(String email) {
        return userRepository.findByEmail(email)
                .map(userFactory::fromUserEntity);
    }
}
