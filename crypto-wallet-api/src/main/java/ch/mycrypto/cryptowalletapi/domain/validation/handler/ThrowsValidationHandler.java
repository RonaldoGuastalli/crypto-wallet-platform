package ch.mycrypto.cryptowalletapi.domain.validation.handler;

import ch.mycrypto.cryptowalletapi.domain.exceptions.DomainException;
import ch.mycrypto.cryptowalletapi.domain.validation.ValidationHandler;
import ch.mycrypto.cryptowalletapi.domain.validation.Error;

import java.util.List;

public class ThrowsValidationHandler implements ValidationHandler {

    @Override
    public ValidationHandler append(final Error anError) {
        throw DomainException.with(anError);
    }

    @Override
    public List<Error> getErrors() {
        return List.of();
    }
}
