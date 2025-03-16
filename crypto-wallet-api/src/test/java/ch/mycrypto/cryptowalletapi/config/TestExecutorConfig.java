package ch.mycrypto.cryptowalletapi.config;

import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@TestConfiguration
public class TestExecutorConfig {

    @Bean
    public ExecutorService synchronousExecutor() {
        return Executors.newSingleThreadExecutor();
    }
}