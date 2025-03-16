package ch.mycrypto.cryptowalletapi.infrastructure.configurations;

import ch.mycrypto.cryptowalletapi.infrastructure.configurations.properties.CoinCapProperties;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.http.client.JdkClientHttpRequestFactory;
import org.springframework.http.converter.FormHttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.client.RestClient;

import java.util.List;

@Configuration(proxyBeanMethods = false)
public class RestClientConfig {

    @Bean
    public RestClient genreRestClientProperties(
            final CoinCapProperties coinCapProperties,
            final ObjectMapper objectMapper
    ) {
        return restClient(coinCapProperties, objectMapper);
    }


    private static RestClient restClient(
            final CoinCapProperties coinCapProperties,
            final ObjectMapper objectMapper
    ) {
        final var factory = new JdkClientHttpRequestFactory();

        return RestClient.builder()
                .baseUrl(coinCapProperties.getBaseUrl())
                .requestFactory(factory)
                .messageConverters(converters -> {
                    converters.removeIf(it -> it instanceof MappingJackson2HttpMessageConverter);
                    converters.add(jsonConverter(objectMapper));
                    converters.add(new FormHttpMessageConverter());
                })
                .build();
    }

    private static MappingJackson2HttpMessageConverter jsonConverter(ObjectMapper objectMapper) {
        final var jsonConverter = new MappingJackson2HttpMessageConverter(objectMapper);
        jsonConverter.setSupportedMediaTypes(List.of(MediaType.APPLICATION_JSON));
        return jsonConverter;
    }
}
