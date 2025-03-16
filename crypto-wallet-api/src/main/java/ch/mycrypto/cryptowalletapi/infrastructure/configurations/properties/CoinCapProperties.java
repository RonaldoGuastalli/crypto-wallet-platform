package ch.mycrypto.cryptowalletapi.infrastructure.configurations.properties;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Getter
@Setter
@Configuration
@ConfigurationProperties("rest-clients.coin-cap")
public class CoinCapProperties {
    private String baseUrl;
}