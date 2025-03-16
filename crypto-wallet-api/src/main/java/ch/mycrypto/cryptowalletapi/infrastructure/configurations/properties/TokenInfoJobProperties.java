package ch.mycrypto.cryptowalletapi.infrastructure.configurations.properties;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Getter
@Setter
@Configuration
@ConfigurationProperties("job.token-info")
public class TokenInfoJobProperties {
    private long fixedRateMs = 21_600_000L;
}