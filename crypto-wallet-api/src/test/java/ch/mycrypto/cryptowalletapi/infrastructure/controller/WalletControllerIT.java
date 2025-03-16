package ch.mycrypto.cryptowalletapi.infrastructure.controller;

import ch.mycrypto.cryptowalletapi.config.BaseTestContainer;
import ch.mycrypto.cryptowalletapi.domain.wallet.token.TokenPriceGateway;
import ch.mycrypto.cryptowalletapi.infrastructure.user.persistence.UserEntity;
import ch.mycrypto.cryptowalletapi.infrastructure.user.persistence.UserRepository;
import ch.mycrypto.cryptowalletapi.infrastructure.wallet.asset.model.AddAssetHttpRequest;
import ch.mycrypto.cryptowalletapi.infrastructure.wallet.model.CreateWalletHttpRequest;
import ch.mycrypto.cryptowalletapi.infrastructure.wallet.model.WalletHttpResponse;
import ch.mycrypto.cryptowalletapi.infrastructure.wallet.persistence.WalletEntity;
import ch.mycrypto.cryptowalletapi.infrastructure.wallet.persistence.WalletRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.client.RestClient;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("testcontainers")
@AutoConfigureMockMvc
class WalletControllerIT extends BaseTestContainer {

    @Mock
    private RestClient restClient;

    @MockitoBean
    private TokenPriceGateway tokenPriceGateway;

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private WalletRepository walletRepository;

    @AfterEach
    void cleanUp() {
        walletRepository.deleteAll();
        userRepository.deleteAll();
    }

    @Test
    @DisplayName("Should create wallet and return it")
    void testCreateWallet_whenValidRequest_shouldReturnWallet() throws Exception {
        // given
        var request = new CreateWalletHttpRequest("user@example.com");

        // when
        var result = mockMvc.perform(post("/v1/wallets")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                // then
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").isNotEmpty())
                .andExpect(jsonPath("$.total").value(0))
                .andExpect(jsonPath("$.assets").isArray())
                .andReturn();

        String content = result.getResponse().getContentAsString();
        WalletHttpResponse response = objectMapper.readValue(content, WalletHttpResponse.class);

        assertThat(response.assets()).isEmpty();
        assertThat(response.total()).isEqualByComparingTo(BigDecimal.ZERO);
    }

    @Test
    @DisplayName("Should return 400 when email is missing")
    void testCreateWallet_whenEmailMissing_shouldReturnBadRequest() throws Exception {
        // given
        var requestJson = "{}";

        // when + then
        mockMvc.perform(post("/v1/wallets")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestJson))
                .andExpect(status().isUnprocessableEntity());
    }

    @Test
    @DisplayName("Should return 409 when email already exists")
    void testCreateWallet_whenEmailAlreadyExists_shouldReturnConflict() throws Exception {
        // given
        var email = "duplicate@example.com";
        userRepository.save(new UserEntity(UUID.randomUUID(), email));

        var request = new CreateWalletHttpRequest(email);

        // when + then
        mockMvc.perform(post("/v1/wallets")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isConflict());
    }

    @Test
    @DisplayName("Should add asset to existing wallet and return updated wallet")
    void testAddAssetToWallet_whenValidRequest_shouldReturnUpdatedWallet() throws Exception {
        // given
        String email = "wallet@example.com";
        var user = userRepository.save(new UserEntity(UUID.randomUUID(), email));
        var walletId = UUID.randomUUID();
        walletRepository.save(new WalletEntity(walletId, user.id(), new ArrayList<>()));

        var request = new AddAssetHttpRequest("BTC", BigDecimal.ONE, BigDecimal.valueOf(50000));
        given(tokenPriceGateway.fetchPriceBySymbol("BTC")).willReturn(BigDecimal.valueOf(50000));


        // when
        var result = mockMvc.perform(patch("/v1/wallets/" + walletId + "/assets")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                // then
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(walletId.toString()))
                .andExpect(jsonPath("$.total").value("2500000000"))
                .andExpect(jsonPath("$.assets").isArray())
                .andExpect(jsonPath("$.assets[0].symbol").value("BTC"))
                .andReturn();

        var body = result.getResponse().getContentAsString();
        WalletHttpResponse response = objectMapper.readValue(body, WalletHttpResponse.class);

        assertThat(response.assets()).hasSize(1);
        assertThat(response.total()).isEqualByComparingTo(BigDecimal.valueOf(2500000000L));
    }

}
