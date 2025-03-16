package ch.mycrypto.cryptowalletapi.infrastructure.configurations.properties;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Getter
@Setter
@Configuration
@ConfigurationProperties(prefix = "job.token-price")
public class TokenPriceJobProperties {
    private long fixedRateMs = 180000;
    private long initialDelayMs = 180000;
}