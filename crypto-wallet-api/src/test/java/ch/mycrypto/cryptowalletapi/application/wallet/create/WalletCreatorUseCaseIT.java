package ch.mycrypto.cryptowalletapi.application.wallet.create;

import ch.mycrypto.cryptowalletapi.config.BaseTestContainer;
import ch.mycrypto.cryptowalletapi.domain.exceptions.EmailAlreadyUsedException;
import ch.mycrypto.cryptowalletapi.domain.user.User;
import ch.mycrypto.cryptowalletapi.domain.user.UserGateway;
import ch.mycrypto.cryptowalletapi.domain.wallet.WalletGateway;
import ch.mycrypto.cryptowalletapi.infrastructure.user.persistence.UserRepository;
import ch.mycrypto.cryptowalletapi.infrastructure.wallet.asset.persistence.AssetRepository;
import ch.mycrypto.cryptowalletapi.infrastructure.wallet.persistence.WalletRepository;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.web.client.RestClient;

import java.math.BigDecimal;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@SpringBootTest
@ActiveProfiles("testcontainers")
class WalletCreatorUseCaseIT extends BaseTestContainer {

    @Mock
    private RestClient restClient;

    @Autowired
    private WalletGateway walletGateway;
    @Autowired
    private UserGateway userGateway;

    @Autowired
    private WalletRepository walletRepository;
    @Autowired
    private AssetRepository assetRepository;
    @Autowired
    private UserRepository userRepository;

    private WalletCreatorUseCase useCase;

    @BeforeEach
    void setUp() {
        useCase = new WalletCreatorUseCase(userGateway, walletGateway);
    }

    @AfterEach
    void cleanUp() {
        walletRepository.deleteAll();
        userRepository.deleteAll();
        assetRepository.deleteAll();
    }

    @Test
    @DisplayName("Should create wallet for new user")
    @Transactional
    void testExecute_whenValidEmail_shouldCreateWallet() {
        // given
        String email = "user@example.com";
        String userId = UUID.randomUUID().toString();
        WalletCreatorUseCase.Input input = new WalletCreatorUseCase.Input(userId, email);

        // when
        WalletCreatorUseCase.Output output = useCase.execute(input);

        // then
        assertThat(output).isNotNull();
        assertThat(output.id()).isNotBlank();
        assertThat(output.total()).isEqualByComparingTo(BigDecimal.ZERO);
        assertThat(output.assets()).isEmpty();

        assertThat(userGateway.findByEmail(email)).isPresent();
        assertThat(walletGateway.findById(output.id())).isPresent();
    }

    @Test
    @DisplayName("Should throw exception when email already exists")
    void testExecute_whenEmailAlreadyUsed_shouldThrowException() {
        // given
        String email = "user@used.com";
        String userId1 = UUID.randomUUID().toString();
        userGateway.save(User.with(userId1, email));

        String userId2 = UUID.randomUUID().toString();
        WalletCreatorUseCase.Input input = new WalletCreatorUseCase.Input(userId2, email);

        // when + then
        assertThatThrownBy(() -> useCase.execute(input))
                .isInstanceOf(EmailAlreadyUsedException.class)
                .hasMessageContaining(email);
    }

}