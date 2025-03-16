package ch.mycrypto.cryptowalletapi.infrastructure.job;

import ch.mycrypto.cryptowalletapi.infrastructure.wallet.token.model.TokenInfoResponse;
import ch.mycrypto.cryptowalletapi.infrastructure.wallet.token.persistence.TokenInfoEntity;
import ch.mycrypto.cryptowalletapi.infrastructure.wallet.token.persistence.TokenInfoRepository;
import ch.mycrypto.cryptowalletapi.infrastructure.wallet.token.restclient.TokenPriceRestClient;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Slf4j
@Component
@RequiredArgsConstructor
public class TokenInfoRefresherJob implements ApplicationRunner {

    private final TokenInfoRepository tokenInfoRepository;
    private final TokenPriceRestClient tokenPriceRestClient;

    private final ExecutorService executor = Executors.newFixedThreadPool(3);

    @Override
    public void run(ApplicationArguments args) {
        log.info("Populating token info on startup...");
        fetchAndUpdateTokenInfo();
    }


    @Scheduled(fixedRateString = "${job.token-info.fixed-rate-ms}")
    public void scheduledRefresh() {
        log.info("Running scheduled token info refresh...");
        fetchAndUpdateTokenInfo();
    }

    private void fetchAndUpdateTokenInfo() {
        List<TokenInfoResponse> tokenInfoList = tokenPriceRestClient.fetchAllTokens();

        for (TokenInfoResponse response : tokenInfoList) {
            executor.submit(() -> {
                try {
                    Optional<TokenInfoEntity> existing = tokenInfoRepository.findBySymbolIgnoreCase(response.symbol());

                    if (existing.isPresent()) {
                        TokenInfoEntity entity = existing.get();
                        if (!entity.getAssetId().equals(response.id())) {
                            entity.setAssetId(response.id());
                            tokenInfoRepository.save(entity);
                            log.info("Updated token info for symbol {}", entity.getSymbol());
                        }
                    } else {
                        TokenInfoEntity newEntity = TokenInfoEntity.builder()
                                .name(response.name())
                                .symbol(response.symbol())
                                .assetId(response.id())
                                .lastUpdated(LocalDateTime.now())
                                .build();
                        tokenInfoRepository.save(newEntity);
                        log.info("Inserted token info for symbol {}", newEntity.getSymbol());
                    }
                } catch (Exception e) {
                    log.error("Error updating token info for symbol {}: {}", response.symbol(), e.getMessage());
                }
            });
        }
    }
}
