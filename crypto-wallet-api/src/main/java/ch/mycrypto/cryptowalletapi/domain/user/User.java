package ch.mycrypto.cryptowalletapi.domain.user;

import ch.mycrypto.cryptowalletapi.domain.validation.Error;
import ch.mycrypto.cryptowalletapi.domain.validation.ValidationHandler;
import ch.mycrypto.cryptowalletapi.domain.validation.handler.ThrowsValidationHandler;

public class User {
    private String id;
    private String email;

    private User(
            final String id,
            final String email
    ) {
        this.id = id;
        this.email = email;
        validate(new ThrowsValidationHandler());
    }

    public static User with(
            final String id,
            final String email
    ) {
        return new User(id, email);
    }

    public static User with(final User anUser) {
        return with(
                anUser.id(),
                anUser.email()
        );
    }

    public void validate(final ValidationHandler handler) {
        if (id == null || id.isBlank()) {
            handler.append(new Error("'id' should not be empty"));
        }

        if (email == null || email.isBlank()) {
            handler.append(new Error("'email' should not be empty"));
        }
    }

    public String id() {
        return id;
    }

    public String email() {
        return email;
    }
}