package ch.mycrypto.cryptowalletapi.domain.user;

import java.util.Optional;

public interface UserGateway {

    User save(User user);

    Optional<User> findByEmail(String email);
}
