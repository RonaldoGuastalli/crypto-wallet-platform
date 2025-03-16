package ch.mycrypto.cryptowalletapi.domain.exceptions;

public class TokenPriceNotFoundException extends NoStacktraceException {

    public TokenPriceNotFoundException(String symbol) {
        super("Token price not found to symbol: " + symbol);
    }

}
