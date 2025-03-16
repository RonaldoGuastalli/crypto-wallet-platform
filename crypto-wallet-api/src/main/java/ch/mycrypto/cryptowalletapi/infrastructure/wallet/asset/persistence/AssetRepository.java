package ch.mycrypto.cryptowalletapi.infrastructure.wallet.asset.persistence;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface AssetRepository extends JpaRepository<AssetEntity, UUID> {

}