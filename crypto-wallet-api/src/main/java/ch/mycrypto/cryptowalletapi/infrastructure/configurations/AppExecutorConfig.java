package ch.mycrypto.cryptowalletapi.infrastructure.configurations;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Configuration
public class AppExecutorConfig {

    public static final int N_THREADS = 3;

    @Bean
    @Profile("!test")
    public ExecutorService fixedThreadPoolExecutor() {
        return Executors.newFixedThreadPool(N_THREADS);
    }
}
