package ch.mycrypto.cryptowalletapi.infrastructure.wallet.token.restclient;


import ch.mycrypto.cryptowalletapi.infrastructure.wallet.token.model.*;

import java.time.Instant;
import java.util.List;

public interface CoinCapClient {

    List<TokenInfoResponse> fetchAllTokens();

    TokenPriceResponse fetchPriceByAssetId(String assetId);

    TokenPriceHistoryResponse fetchPriceHistory(String assetId, Instant start, Instant end);

}
