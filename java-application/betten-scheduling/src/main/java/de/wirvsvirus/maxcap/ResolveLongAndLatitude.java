package de.wirvsvirus.maxcap;

import de.wirvsvirus.maxcap.npgeoconsumer.event.StateAndCounty;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
@Slf4j
@RequiredArgsConstructor
public class ResolveLongAndLatitude {

  private final RestTemplate restTemplate;

  @Value("${location.uri}")
  private String locationUri;

  public StateAndCounty getStateAndCounty(Double lat, Double lon) {
    return resolveLatAndLon(restTemplate, lat, lon);
  }

  public StateAndCounty resolveLatAndLon(
      @Qualifier("npgeoApiClientTemplate") RestTemplate restTemplate, Double lat, Double lon) {
    locationUri += "API_KEY&lat=" + lat + "&lon=" + lon + "&format=json";
    return restTemplate.getForObject(locationUri, StateAndCounty.class);
  }
}
