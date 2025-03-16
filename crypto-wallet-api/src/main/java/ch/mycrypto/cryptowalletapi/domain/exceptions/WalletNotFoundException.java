package ch.mycrypto.cryptowalletapi.domain.exceptions;

public class WalletNotFoundException extends RuntimeException {
    public WalletNotFoundException(String walletId) {
        super("Wallet not found to wallet id " + walletId);
    }
}