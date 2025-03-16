package ch.mycrypto.cryptowalletapi.config;

import org.junit.jupiter.api.TestInstance;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Testcontainers;

@Testcontainers
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@ComponentScan(basePackages = "{ch.mycrypto.cryptowalletapi.*}")
public abstract class BaseTestContainer {

    static final PostgreSQLContainer<?> POSTGRESQL_CONTAINER =
            new PostgreSQLContainer<>("postgres:15.5")
                    .withDatabaseName("crypto-wallet")
                    .withUsername("test")
                    .withPassword("test")
                    .withReuse(false);


    static {
        POSTGRESQL_CONTAINER.start();
        Runtime.getRuntime().addShutdownHook(new Thread(POSTGRESQL_CONTAINER::stop));
    }

    @DynamicPropertySource
    static void registerProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", POSTGRESQL_CONTAINER::getJdbcUrl);
        registry.add("spring.datasource.username", POSTGRESQL_CONTAINER::getUsername);
        registry.add("spring.datasource.password", POSTGRESQL_CONTAINER::getPassword);
    }
}
