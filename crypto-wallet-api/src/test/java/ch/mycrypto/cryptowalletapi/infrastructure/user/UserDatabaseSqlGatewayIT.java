package ch.mycrypto.cryptowalletapi.infrastructure.user;

import ch.mycrypto.cryptowalletapi.config.BaseTestContainer;
import ch.mycrypto.cryptowalletapi.domain.user.User;
import ch.mycrypto.cryptowalletapi.infrastructure.mapper.user.UserMapper;
import ch.mycrypto.cryptowalletapi.infrastructure.user.persistence.UserRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;

@SpringBootTest
@ActiveProfiles(value = "testcontainers")
class UserDatabaseSqlGatewayIT extends BaseTestContainer {

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private UserFactory userFactory;
    @Autowired
    private UserMapper userMapper;

    private UserDatabaseSqlGateway userDatabaseSqlGateway;

    @BeforeEach
    void setUp() {
        userDatabaseSqlGateway = new UserDatabaseSqlGateway(
                userRepository,
                userFactory,
                userMapper
        );
    }

    @AfterEach
    void cleanUp() {
        userRepository.deleteAll();
    }

    @Test
    @DisplayName("Should create an user")
    void testSave_whenValidUser_shouldCreateAnUser() {
        // given
        final var user = User.with(
                UUID.randomUUID().toString(),
                "user@email.com"
        );

        // when
        User userSaved = userDatabaseSqlGateway.save(user);

        // then
        assertThat(userSaved.id()).isEqualTo(user.id());
        assertThat(userSaved.email()).isEqualTo(user.email());
    }

    @Test
    @DisplayName("Should return user when email exists")
    void testFindByEmail_whenUserExists_shouldReturnUser() {
        // given
        final var user = User.with(UUID.randomUUID().toString(), "test@email.com");
        userDatabaseSqlGateway.save(user);

        // when
        Optional<User> result = userDatabaseSqlGateway.findByEmail("test@email.com");

        // then
        assertThat(result).isPresent();
        assertThat(result.get().email()).isEqualTo("test@email.com");
    }

    @Test
    @DisplayName("Should return empty when user not found by email")
    void testFindByEmail_whenUserDoesNotExist_shouldReturnEmpty() {
        // when
        Optional<User> result = userDatabaseSqlGateway.findByEmail("unknown@email.com");

        // then
        assertThat(result).isEmpty();
    }

    @Test
    @DisplayName("Should update user when saving the same ID twice")
    void testSave_whenSavingTwice_shouldUpdateUser() {
        // given
        String id = UUID.randomUUID().toString();
        User user1 = User.with(id, "original@email.com");
        User user2 = User.with(id, "updated@email.com");

        // when
        userDatabaseSqlGateway.save(user1);
        User updated = userDatabaseSqlGateway.save(user2);

        // then
        assertThat(updated.id()).isEqualTo(id);
        assertThat(updated.email()).isEqualTo("updated@email.com");
        assertThat(userRepository.findAll()).hasSize(1);
    }

    @Test
    @DisplayName("Should throw exception when saving user with duplicate email")
    void testSave_whenEmailIsDuplicate_shouldThrowException() {
        // given
        String email = "duplicate@email.com";
        User user1 = User.with(UUID.randomUUID().toString(), email);
        User user2 = User.with(UUID.randomUUID().toString(), email);

        userDatabaseSqlGateway.save(user1);

        // when + then
        assertThatThrownBy(() -> userDatabaseSqlGateway.save(user2))
                .isInstanceOf(Exception.class)
                .hasMessageContaining("email");
    }

}