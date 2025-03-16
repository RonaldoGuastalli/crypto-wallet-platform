package ch.mycrypto.cryptowalletapi.application;

public abstract class UseCase<IN, OUT> {

    public abstract OUT execute(IN anIn);
}