package ch.mycrypto.cryptowalletapi.unit;

import ch.mycrypto.cryptowalletapi.domain.exceptions.DomainException;
import ch.mycrypto.cryptowalletapi.domain.wallet.asset.Asset;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class AssetTest {

    @Test
    @DisplayName("Should create asset with valid data")
    void testWith_whenValidData_shouldCreateAsset() {
        // given
        String symbol = "BTC";
        BigDecimal quantity = BigDecimal.valueOf(2);
        BigDecimal price = BigDecimal.valueOf(30000);

        // when
        Asset asset = Asset.with(symbol, quantity, price);

        // then
        assertThat(asset.symbol()).isEqualTo(symbol);
        assertThat(asset.quantity()).isEqualByComparingTo(quantity);
        assertThat(asset.price()).isEqualByComparingTo(price);
    }

    @Test
    @DisplayName("Should throw exception for invalid data")
    void testWith_whenInvalidData_shouldThrowException() {
        // when + then
        assertThatThrownBy(() -> Asset.with(null, BigDecimal.ONE, BigDecimal.TEN))
                .isInstanceOf(DomainException.class);

        assertThatThrownBy(() -> Asset.with("   ", BigDecimal.ONE, BigDecimal.TEN))
                .isInstanceOf(DomainException.class);

        assertThatThrownBy(() -> Asset.with("BTC", BigDecimal.ZERO, BigDecimal.TEN))
                .isInstanceOf(DomainException.class);

        assertThatThrownBy(() -> Asset.with("BTC", BigDecimal.ONE, BigDecimal.ZERO))
                .isInstanceOf(DomainException.class);
    }

    @Test
    @DisplayName("Should calculate correct asset value")
    void testGetValue_whenCalled_shouldReturnValue() {
        // given
        Asset asset = Asset.with("BTC", BigDecimal.valueOf(2), BigDecimal.valueOf(35000));

        // when
        BigDecimal value = asset.getValue();

        // then
        assertThat(value).isEqualByComparingTo(BigDecimal.valueOf(70000));
    }

    @Test
    @DisplayName("Should increase quantity and return new asset")
    void testIncreaseQuantity_whenCalled_shouldReturnNewAssetWithAddedQuantity() {
        // given
        Asset original = Asset.with("BTC", BigDecimal.valueOf(1), BigDecimal.valueOf(50000));

        // when
        Asset updated = original.increaseQuantity(BigDecimal.valueOf(0.5));

        // then
        assertThat(updated.quantity()).isEqualByComparingTo(BigDecimal.valueOf(1.5));
        assertThat(updated.symbol()).isEqualTo("BTC");
        assertThat(updated.price()).isEqualByComparingTo(BigDecimal.valueOf(50000));
    }

    @Test
    @DisplayName("Should update price of asset")
    void testUpdatePrice_whenCalled_shouldUpdatePrice() {
        // given
        Asset asset = Asset.with("BTC", BigDecimal.valueOf(1), BigDecimal.valueOf(50000));

        // when
        asset.updatePrice(BigDecimal.valueOf(60000));

        // then
        assertThat(asset.price()).isEqualByComparingTo(BigDecimal.valueOf(60000));
    }
}
