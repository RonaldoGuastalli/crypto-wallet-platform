package ch.mycrypto.cryptowalletapi.infrastructure.wallet.model;

import jakarta.validation.constraints.Email;

public record CreateWalletHttpRequest(
        @Email(message = "Email must be valid")
        String email
) {
}