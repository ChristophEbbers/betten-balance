package de.wirvsvirus.maxcap;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.client.RestTemplate;

import java.time.Duration;
import java.util.Arrays;

@Configuration
public class NpgeoClientConfiguration {

  @Bean("npgeoApiClientTemplate")
  RestTemplate buildRestTemplate(
      RestTemplateBuilder restTemplateBuilder, ObjectMapper objectMapper) {
    var responseJsonConverter = new MappingJackson2HttpMessageConverter(objectMapper);
    responseJsonConverter.setSupportedMediaTypes(
        Arrays.asList(
            MediaType.parseMediaType("text/plain;charset=utf-8"),
            MediaType.APPLICATION_JSON,
            MediaType.APPLICATION_OCTET_STREAM));
    return restTemplateBuilder
        .messageConverters(responseJsonConverter)
        .setConnectTimeout(Duration.ofSeconds(60L))
        .setReadTimeout(Duration.ofSeconds(60))
        .build();
  }
}
