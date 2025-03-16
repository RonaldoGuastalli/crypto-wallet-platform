package ch.mycrypto.cryptowalletapi.domain.exceptions;

public class AssetIdNotFoundException extends NoStacktraceException {

    public AssetIdNotFoundException(String symbol) {
        super("Asset id not found to symbol: " + symbol);
    }

}
