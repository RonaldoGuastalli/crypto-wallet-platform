package ch.mycrypto.cryptowalletapi.unit;

import ch.mycrypto.cryptowalletapi.domain.exceptions.DomainException;
import ch.mycrypto.cryptowalletapi.domain.wallet.Wallet;
import ch.mycrypto.cryptowalletapi.domain.wallet.asset.Asset;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.Set;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;

class WalletTest {

    @Test
    @DisplayName("Should throw exception for invalid user ID")
    void testWith_whenUserIdIsInvalid_shouldThrowException() {
        // given
        String validId = UUID.randomUUID().toString();

        // when + then
        assertThatThrownBy(() -> Wallet.with(validId, null, null))
                .isInstanceOf(DomainException.class)
                .hasMessageContaining("'user Id' should not be empty");

        // when + then
        assertThatThrownBy(() -> Wallet.with(validId, " ", null))
                .isInstanceOf(DomainException.class)
                .hasMessageContaining("'user Id' should not be empty");
    }

    @Test
    @DisplayName("Should throw exception for invalid wallet ID")
    void testWith_whenWalletIdIsInvalid_shouldThrowException() {
        // given
        String validUserId = UUID.randomUUID().toString();

        // when + then
        assertThatThrownBy(() -> Wallet.with(null, validUserId, null))
                .isInstanceOf(DomainException.class)
                .hasMessageContaining("'id' should not be empty");

        // when + then
        assertThatThrownBy(() -> Wallet.with("   ", validUserId, null))
                .isInstanceOf(DomainException.class)
                .hasMessageContaining("'id' should not be empty");
    }

    @Test
    @DisplayName("Should create wallet with valid inputs")
    void testWith_whenValidInputs_shouldCreateWallet() {
        // given
        String walletId = UUID.randomUUID().toString();
        String userId = UUID.randomUUID().toString();

        // when
        Wallet wallet = Wallet.with(walletId, userId, null);

        // then
        assertThat(wallet).isNotNull();
        assertThat(wallet.id()).isEqualTo(walletId);
        assertThat(wallet.userId()).isEqualTo(userId);
        assertThat(wallet.assets()).isEmpty();
    }

    @Test
    @DisplayName("Should add new asset to wallet")
    void testAddAsset_whenNewAsset_shouldAddToWallet() {
        // given
        Wallet wallet = Wallet.with(UUID.randomUUID().toString(), UUID.randomUUID().toString(), null);
        Asset asset = Asset.with("BTC", BigDecimal.valueOf(1), BigDecimal.valueOf(50000));

        // when
        wallet.addAsset(asset);

        // then
        assertThat(wallet.assets()).contains(asset);
    }

    @Test
    @DisplayName("Should update existing asset quantity")
    void testUpdateAsset_whenAssetExists_shouldIncreaseQuantity() {
        // given
        Asset btc = Asset.with("BTC", BigDecimal.valueOf(1), BigDecimal.valueOf(50000));
        Wallet wallet = Wallet.with(UUID.randomUUID().toString(), UUID.randomUUID().toString(), Set.of(btc));
        Asset newBtc = Asset.with("BTC", BigDecimal.valueOf(0.5), BigDecimal.valueOf(25000));

        // when
        wallet.updateAsset(newBtc);

        // then
        assertThat(wallet.assets()).hasSize(1);
        assertThat(wallet.assets().iterator().next().quantity()).isEqualTo(BigDecimal.valueOf(1.5));
    }

    @Test
    @DisplayName("Should calculate total wallet value")
    void testGetTotalValue_whenCalled_shouldReturnWalletTotalValue() {
        // given
        Asset btc = Asset.with("BTC", BigDecimal.valueOf(1), BigDecimal.valueOf(50000));
        Asset eth = Asset.with("ETH", BigDecimal.valueOf(2), BigDecimal.valueOf(7000));
        Wallet wallet = Wallet.with(UUID.randomUUID().toString(), UUID.randomUUID().toString(), Set.of(btc, eth));

        // when
        BigDecimal total = wallet.getTotalValue();

        // then
        assertThat(total).isEqualByComparingTo(BigDecimal.valueOf(64000));
    }

    @Test
    @DisplayName("Should add or update asset based on existence")
    void testAddOrUpdateAsset_whenAssetExistsOrNot_shouldBehaveAccordingly() {
        // given
        Asset btc = Asset.with("BTC", BigDecimal.valueOf(1), BigDecimal.valueOf(50000));
        Wallet wallet = Wallet.with(UUID.randomUUID().toString(), UUID.randomUUID().toString(), Set.of(btc));

        // when - update existing BTC
        Asset btcUpdate = Asset.with("BTC", BigDecimal.valueOf(1), BigDecimal.valueOf(60000));
        wallet.addOrUpdateAsset(btcUpdate);

        // then
        assertThat(wallet.assets().size()).isEqualTo(1);
        assertThat(wallet.assets().iterator().next().quantity()).isEqualTo(BigDecimal.valueOf(2));
        assertThat(wallet.assets().iterator().next().price()).isEqualTo(BigDecimal.valueOf(60000));

        // when - add new ETH
        Asset eth = Asset.with("ETH", BigDecimal.valueOf(2), BigDecimal.valueOf(7000));
        wallet.addOrUpdateAsset(eth);

        // then
        assertThat(wallet.assets().size()).isEqualTo(2);
        assertThat(wallet.assets()).anyMatch(a -> a.symbol().equals("ETH"));
    }

}
