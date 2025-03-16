package ch.mycrypto.cryptowalletapi.infrastructure.wallet.token.model;

import java.util.List;

public record TokenListResponse(
        List<TokenInfoResponse> data
) {}