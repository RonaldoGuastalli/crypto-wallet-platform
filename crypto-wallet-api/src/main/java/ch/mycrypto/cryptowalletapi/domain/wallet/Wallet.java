package ch.mycrypto.cryptowalletapi.domain.wallet;

import ch.mycrypto.cryptowalletapi.domain.validation.ValidationHandler;
import ch.mycrypto.cryptowalletapi.domain.validation.Error;
import ch.mycrypto.cryptowalletapi.domain.validation.handler.ThrowsValidationHandler;
import ch.mycrypto.cryptowalletapi.domain.wallet.asset.Asset;

import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;

public class Wallet {
    private String id;
    private String userId;
    private Set<Asset> assets;

    private Wallet(
            String id,
            String userId,
            Set<Asset> assets
    ) {
        this.id = id;
        this.userId = userId;
        this.assets = assets != null ? assets : new HashSet<>();
        validate(new ThrowsValidationHandler());
    }

    public static Wallet with(
            final String id,
            final String userId,
            final Set<Asset> assets

    ) {
        return new Wallet(id, userId, assets);
    }

    public static Wallet with(final Wallet aWallet) {
        return with(
                aWallet.id(),
                aWallet.userId(),
                aWallet.assets()
        );
    }

    public void validate(final ValidationHandler handler) {
        if (id == null || id.isBlank()) {
            handler.append(new Error("'id' should not be empty"));
        }

        if (userId == null || userId.isBlank()) {
            handler.append(new Error("'user Id' should not be empty"));
        }
    }

    public Wallet updateAsset(Asset updatedAsset) {
        this.assets = assets.stream()
                .map(asset -> asset.symbol().equals(updatedAsset.symbol())
                        ? asset.increaseQuantity(updatedAsset.quantity()).updatePrice(updatedAsset.price())
                        : asset)
                .collect(Collectors.toSet());
        return this;
    }

    public Wallet addAsset(Asset newAsset) {
        Set<Asset> newAssets = new HashSet<>(this.assets);
        newAssets.add(newAsset);
        this.assets = newAssets;
        return this;
    }

    public Wallet addOrUpdateAsset(Asset newAsset) {
        boolean exists = assets.stream().anyMatch(a -> a.symbol().equals(newAsset.symbol()));
        return exists ? updateAsset(newAsset) : addAsset(newAsset);
    }

    public BigDecimal getTotalValue() {
        return assets.stream()
                .map(Asset::getValue)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    public String id() {
        return id;
    }

    public String userId() {
        return userId;
    }

    public Set<Asset> assets() {
        return assets;
    }
}
