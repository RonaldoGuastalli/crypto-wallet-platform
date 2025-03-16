package ch.mycrypto.cryptowalletapi.infrastructure.wallet.token.persistence;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface TokenPriceRepository extends JpaRepository<TokenPriceEntity, UUID> {

}