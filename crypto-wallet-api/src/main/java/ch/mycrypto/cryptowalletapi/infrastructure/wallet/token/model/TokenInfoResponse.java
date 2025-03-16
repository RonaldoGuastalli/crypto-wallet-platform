package ch.mycrypto.cryptowalletapi.infrastructure.wallet.token.model;

public record TokenInfoResponse(
        String id,
        String name,
        String symbol
) {
}