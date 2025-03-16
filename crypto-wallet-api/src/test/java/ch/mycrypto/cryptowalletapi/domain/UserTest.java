package ch.mycrypto.cryptowalletapi.domain;

import ch.mycrypto.cryptowalletapi.domain.exceptions.DomainException;
import ch.mycrypto.cryptowalletapi.domain.user.User;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class UserTest {

    @Test
    @DisplayName("Should create User with valid email")
    void testCreate_whenValidEmail_shouldCreateUser() {
        // given
        final var expectedID = UUID.randomUUID().toString();
        final var email = "user@example.com";

        // when
        User user = User.with(expectedID, email);

        // then
        assertThat(user.email()).isEqualTo(email);
        assertThat(user.id()).isNotBlank();
    }

    @Test
    @DisplayName("Should throw exception when email is null or blank")
    void testCreate_whenInvalidEmail_shouldThrowException() {
        // given + when + then
        assertThatThrownBy(() ->
                User.with(UUID.randomUUID().toString(), null))
                .isInstanceOf(DomainException.class)
                .hasMessageContaining("email");

        assertThatThrownBy(() -> User.with(UUID.randomUUID().toString(), " "))
                .isInstanceOf(DomainException.class);
    }
}
