package ch.mycrypto.cryptowalletapi.infrastructure.wallet;

import ch.mycrypto.cryptowalletapi.config.BaseTestContainer;
import ch.mycrypto.cryptowalletapi.domain.user.User;
import ch.mycrypto.cryptowalletapi.domain.user.UserGateway;
import ch.mycrypto.cryptowalletapi.domain.wallet.Wallet;
import ch.mycrypto.cryptowalletapi.domain.wallet.asset.Asset;
import ch.mycrypto.cryptowalletapi.infrastructure.mapper.wallet.WalletMapper;
import ch.mycrypto.cryptowalletapi.infrastructure.user.persistence.UserRepository;
import ch.mycrypto.cryptowalletapi.infrastructure.wallet.asset.persistence.AssetRepository;
import ch.mycrypto.cryptowalletapi.infrastructure.wallet.persistence.WalletRepository;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@ActiveProfiles("testcontainers")
class WalletDatabaseSqlGatewayIT extends BaseTestContainer {

    @Autowired
    private UserGateway userGateway;
    @Autowired
    private WalletMapper walletMapper;

    @Autowired
    private WalletRepository walletRepository;
    @Autowired
    private AssetRepository assetRepository;
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private WalletFactory walletFactory;

    private WalletDatabaseSqlGateway gateway;

    @BeforeEach
    void setUp() {
        gateway = new WalletDatabaseSqlGateway(walletRepository, walletMapper, walletFactory);
    }

    @AfterEach
    void cleanUp() {
        walletRepository.deleteAll();
        userRepository.deleteAll();
        assetRepository.deleteAll();
    }

    @Test
    @DisplayName("Should persist wallet successfully")
    void testSave_whenValidWallet_shouldPersistSuccessfully() {
        // given
        User user = User.with(UUID.randomUUID().toString(), "user@email.com");
        userGateway.save(user);
        Wallet wallet = Wallet.with(UUID.randomUUID().toString(), user.id(), null);

        // when
        Wallet saved = gateway.save(wallet);

        // then
        assertThat(saved).isNotNull();
        assertThat(saved.id()).isEqualTo(wallet.id());
        assertThat(saved.userId()).isEqualTo(wallet.userId());
    }

    @Test
    @DisplayName("Should find wallet by ID")
    @Transactional
    void testFindById_whenWalletExists_shouldReturnWallet() {
        // given
        User user = User.with(UUID.randomUUID().toString(), "user@email.com");
        userGateway.save(user);
        Wallet wallet = Wallet.with(UUID.randomUUID().toString(), user.id(), Set.of());
        gateway.save(wallet);

        // when
        Optional<Wallet> found = gateway.findById(wallet.id());

        // then
        assertThat(found).isPresent();
        assertThat(found.get().id()).isEqualTo(wallet.id());
    }

    @Test
    @DisplayName("Should return empty when wallet not found")
    void testFindById_whenWalletDoesNotExist_shouldReturnEmpty() {
        // when
        Optional<Wallet> found = gateway.findById(UUID.randomUUID().toString());

        // then
        assertThat(found).isEmpty();
    }

    @Test
    @DisplayName("Should return all unique symbols from all wallets")
    void testFindAllUniqueSymbols_whenWalletsHaveAssets_shouldReturnDistinctSymbols() {
        // given
        final var userId = UUID.randomUUID().toString();
        User user = User.with(userId, "user@email");
        userGateway.save(user);

        // BTC in wallet1
        Wallet wallet1 = Wallet.with(UUID.randomUUID().toString(), userId,
                Set.of(Asset.with("BTC", BigDecimal.ONE, BigDecimal.valueOf(50000))));

        // ETH in wallet2
        Wallet wallet2 = Wallet.with(UUID.randomUUID().toString(), userId,
                Set.of(Asset.with("ETH", BigDecimal.ONE, BigDecimal.valueOf(2500))));

        // BTC again in wallet3 (duplicate symbol)
        Wallet wallet3 = Wallet.with(UUID.randomUUID().toString(), userId,
                Set.of(Asset.with("BTC", BigDecimal.ONE, BigDecimal.valueOf(60000))));

        //
        gateway.save(wallet1);
        gateway.save(wallet2);
        gateway.save(wallet3);

        // when
        List<String> symbols = gateway.findAllUniqueSymbols();

        // then
        assertThat(symbols).containsExactlyInAnyOrder("BTC", "ETH");
    }
}
