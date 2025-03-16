package ch.mycrypto.cryptowalletapi.domain.exceptions;

public class EmailAlreadyUsedException extends NoStacktraceException {

    public EmailAlreadyUsedException(String email) {
        super("Wallet already exists for email: " + email);
    }

}
