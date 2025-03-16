package ch.mycrypto.cryptowalletapi.infrastructure.wallet;

import ch.mycrypto.cryptowalletapi.domain.wallet.Wallet;
import ch.mycrypto.cryptowalletapi.domain.wallet.WalletGateway;
import ch.mycrypto.cryptowalletapi.infrastructure.mapper.wallet.WalletMapper;
import ch.mycrypto.cryptowalletapi.infrastructure.wallet.persistence.WalletRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
@RequiredArgsConstructor
public class WalletDatabaseSqlGateway implements WalletGateway {

    private final WalletRepository walletRepository;
    private final WalletMapper mapper;
    private final WalletFactory walletFactory;

    @Override
    public Wallet save(Wallet wallet) {
        var entity = walletFactory.toEntity(walletFactory.toDto(wallet));
        var saved = walletRepository.save(entity);
        return walletFactory.fromDto(walletFactory.toDto(saved));
    }

    @Override
    public Optional<Wallet> findById(String id) {
        return walletRepository.findById(UUID.fromString(id))
                .map(mapper::toDto)
                .map(walletFactory::fromDto);
    }

    @Override
    public List<String> findAllUniqueSymbols() {
        return walletRepository.findAllUniqueSymbols();
    }

}
