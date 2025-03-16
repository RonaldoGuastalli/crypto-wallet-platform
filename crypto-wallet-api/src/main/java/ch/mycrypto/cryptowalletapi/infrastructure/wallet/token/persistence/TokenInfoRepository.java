package ch.mycrypto.cryptowalletapi.infrastructure.wallet.token.persistence;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface TokenInfoRepository extends JpaRepository<TokenInfoEntity, UUID> {

    Optional<TokenInfoEntity> findBySymbolIgnoreCase(String symbol);

}
