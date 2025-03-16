package ch.mycrypto.cryptowalletapi.domain.validation;

import java.util.List;

public interface ValidationHandler {

    ValidationHandler append(Error anError);

    List<Error> getErrors();

    interface Validation<T> {
        T validate();
    }
}
