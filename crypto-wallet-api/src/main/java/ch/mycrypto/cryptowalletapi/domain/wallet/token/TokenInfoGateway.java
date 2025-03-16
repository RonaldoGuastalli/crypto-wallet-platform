package ch.mycrypto.cryptowalletapi.domain.wallet.token;

import java.util.Optional;

public interface TokenInfoGateway {

    Optional<String> findAssetIdBySymbol(String symbol);

}
