package ch.mycrypto.cryptowalletapi.infrastructure.wallet.token.model;

public record TokenPriceResponse(
        String id,
        String symbol,
        String name,
        String priceUsd
) {}