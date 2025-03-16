package ch.mycrypto.cryptowalletapi.unit;

import ch.mycrypto.cryptowalletapi.domain.exceptions.DomainException;
import ch.mycrypto.cryptowalletapi.domain.wallet.token.TokenPrice;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class TokenPriceTest {

    @Test
    @DisplayName("Should create TokenPrice with valid data")
    void testWith_whenValidInput_shouldCreateTokenPrice() {
        // given
        String symbol = "ETH";
        BigDecimal price = BigDecimal.valueOf(3500.75);
        LocalDateTime timestamp = LocalDateTime.now();

        // when
        TokenPrice tokenPrice = TokenPrice.with(symbol, price, timestamp);

        // then
        assertThat(tokenPrice.symbol()).isEqualTo(symbol);
        assertThat(tokenPrice.price()).isEqualByComparingTo(price);
        assertThat(tokenPrice.timestamp()).isEqualTo(timestamp);
    }

    @Test
    @DisplayName("Should throw exception when symbol is null or blank")
    void testWith_whenSymbolIsInvalid_shouldThrowException() {
        // when + then
        assertThatThrownBy(() -> TokenPrice.with(null, BigDecimal.TEN, LocalDateTime.now()))
                .isInstanceOf(DomainException.class);

        assertThatThrownBy(() -> TokenPrice.with(" ", BigDecimal.TEN, LocalDateTime.now()))
                .isInstanceOf(DomainException.class);
    }

    @Test
    @DisplayName("Should throw exception when price is invalid")
    void testWith_whenPriceIsInvalid_shouldThrowException() {
        // when + then
        assertThatThrownBy(() -> TokenPrice.with("BTC", BigDecimal.ZERO, LocalDateTime.now()))
                .isInstanceOf(DomainException.class);

        assertThatThrownBy(() -> TokenPrice.with("BTC", BigDecimal.valueOf(-1), LocalDateTime.now()))
                .isInstanceOf(DomainException.class);
    }

    @Test
    @DisplayName("Should throw exception when timestamp is null")
    void testWith_whenTimestampIsNull_shouldThrowException() {
        // when + then
        assertThatThrownBy(() -> TokenPrice.with("BTC", BigDecimal.TEN, null))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("timestamp");
    }

}
