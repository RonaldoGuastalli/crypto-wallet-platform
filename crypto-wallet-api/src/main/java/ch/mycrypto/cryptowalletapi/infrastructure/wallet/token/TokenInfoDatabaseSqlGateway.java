package ch.mycrypto.cryptowalletapi.infrastructure.wallet.token;


import ch.mycrypto.cryptowalletapi.domain.wallet.token.TokenInfoGateway;
import ch.mycrypto.cryptowalletapi.infrastructure.wallet.token.persistence.TokenInfoEntity;
import ch.mycrypto.cryptowalletapi.infrastructure.wallet.token.persistence.TokenInfoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class TokenInfoDatabaseSqlGateway implements TokenInfoGateway {

    private final TokenInfoRepository tokenInfoRepository;

    @Override
    public Optional<String> findAssetIdBySymbol(String symbol) {
        return tokenInfoRepository
                .findBySymbolIgnoreCase(symbol)
                .map(TokenInfoEntity::getAssetId);
    }


}
