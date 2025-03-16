package ch.mycrypto.cryptowalletapi.domain.wallet;

import java.util.List;
import java.util.Optional;

public interface WalletGateway {

    Wallet save(Wallet wallet);

    Optional<Wallet> findById(String id);

    List<String> findAllUniqueSymbols();

}
