package ch.mycrypto.cryptowalletapi.infrastructure.wallet.token.restclient;

import ch.mycrypto.cryptowalletapi.domain.exceptions.TokenPriceNotFoundException;
import ch.mycrypto.cryptowalletapi.infrastructure.wallet.token.model.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.RestClientException;

import java.time.Instant;
import java.util.List;

@Slf4j
@Component
public class TokenPriceRestClient implements CoinCapClient {

    private final RestClient restClient;

    public TokenPriceRestClient(RestClient restClient) {
        this.restClient = restClient;
    }

    @Override
    public List<TokenInfoResponse> fetchAllTokens() {
        try {
            var response = restClient.get()
                    .uri("/v2/assets")
                    .retrieve()
                    .body(TokenListResponse.class);

            if (response == null || response.data() == null) {
                throw new TokenPriceNotFoundException("Could not retrieve tokens list from CoinCap API.");
            }

            return response.data();

        } catch (RestClientException ex) {
            log.error("Failed to fetch all tokens from CoinCap", ex);
            throw new TokenPriceNotFoundException("Failed to fetch token list: " + ex.getMessage());
        }
    }

    @Override
    public TokenPriceResponse fetchPriceByAssetId(String assetId) {
        try {
            var response = restClient.get()
                    .uri("/v2/assets/{id}", assetId)
                    .retrieve()
                    .body(SingleTokenResponse.class);

            if (response == null || response.data() == null) {
                throw new TokenPriceNotFoundException(assetId);
            }

            return response.data();

        } catch (HttpClientErrorException.NotFound ex) {
            throw new TokenPriceNotFoundException(assetId);
        } catch (RestClientException ex) {
            log.error("Failed to fetch token price for assetId={}", assetId, ex);
            throw new TokenPriceNotFoundException("Failed to fetch token price for: " + assetId);
        }
    }

    @Override
    public TokenPriceHistoryResponse fetchPriceHistory(String assetId, Instant start, Instant end) {
        try {
            String startSec = String.valueOf(start.getEpochSecond());
            String endSec = String.valueOf(end.getEpochSecond());

            var response = restClient.get()
                    .uri(uriBuilder -> uriBuilder
                            .path("/v2/assets/{id}/history")
                            .queryParam("interval", "d1")
                            .queryParam("start", startSec)
                            .queryParam("end", endSec)
                            .build(assetId))
                    .retrieve()
                    .body(TokenPriceHistoryResponse.class);

            if (response == null || response.data() == null || response.data().isEmpty()) {
                throw new TokenPriceNotFoundException(assetId);
            }

            return response;

        } catch (HttpClientErrorException.Unauthorized ex) {
            log.warn("Unauthorized request to CoinCap for assetId={}, start={}, end={}", assetId, start, end);
            throw new TokenPriceNotFoundException("Unauthorized to fetch history for: " + assetId);
        } catch (RestClientException ex) {
            log.error("Failed to fetch price history for assetId={}, start={}, end={}", assetId, start, end, ex);
            throw new TokenPriceNotFoundException("Error retrieving history for asset: " + assetId);
        }
    }
}
