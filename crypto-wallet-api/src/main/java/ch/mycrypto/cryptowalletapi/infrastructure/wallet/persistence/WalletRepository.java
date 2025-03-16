package ch.mycrypto.cryptowalletapi.infrastructure.wallet.persistence;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface WalletRepository extends JpaRepository<WalletEntity, UUID> {

    Optional<WalletEntity> findById(UUID id);

    @Query("select distinct a.symbol from AssetEntity a")
    List<String> findAllUniqueSymbols();

}