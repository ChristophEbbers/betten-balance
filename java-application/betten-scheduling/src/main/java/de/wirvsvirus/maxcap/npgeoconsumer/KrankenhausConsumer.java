package de.wirvsvirus.maxcap.npgeoconsumer;

import de.wirvsvirus.maxcap.Krankenhaus;
import de.wirvsvirus.maxcap.ResolveLongAndLatitude;
import de.wirvsvirus.maxcap.npgeoconsumer.event.ApiNpgeoKrankenhaus;
import de.wirvsvirus.maxcap.npgeoconsumer.repository.KrankenhausRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.client.RestTemplate;

import java.time.Instant;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/maxcap")
@Component
@Slf4j
@RequiredArgsConstructor
public class KrankenhausConsumer {

  private final RestTemplate restTemplate;
  private final KrankenhausRepository krankenhausRepository;
  private final ResolveLongAndLatitude resolveLongAndLatitude;

  @Value("${npgeo.krankenhaus.uri}")
  private String npgeoKrankenhausBaseUri;

  // @Scheduled(initialDelayString = "PT1M", fixedDelayString = "10000000")
  @GetMapping("/krankenhaus")
  public void consumeNpgeoKrankenhaeuser() {
    log.info("Consume Krankenhaus from {}", npgeoKrankenhausBaseUri);
    saveKrankenhaeuserFromApi(restTemplate);
  }

  private void saveKrankenhaeuserFromApi(
      @Qualifier("npgeoApiClientTemplate") RestTemplate restTemplate) {
    List<Krankenhaus> krankenhaus =
        toKrankenhausModel(
            Objects.requireNonNull(
                restTemplate.getForObject(npgeoKrankenhausBaseUri, ApiNpgeoKrankenhaus.class)),
            restTemplate);
    krankenhaus.forEach(krankenhausRepository::save);
    log.info("Saved {} Krankenhäuser", krankenhaus.size());
  }

  private List<Krankenhaus> toKrankenhausModel(
      ApiNpgeoKrankenhaus apiNpgeoKrankenhaus, RestTemplate restTemplate) {
    return apiNpgeoKrankenhaus.getFeatures().stream()
        .map(
            feature -> {
              String name = feature.getProperties().getName();
              String amenty = feature.getProperties().getAmenity();
              String healthcare = feature.getProperties().getHealthcare();
              String operator = feature.getProperties().getOperator();
              String telefonnummer = feature.getProperties().getContact_phone();
              String webseite = feature.getProperties().getContact_website();
              String strasse = feature.getProperties().getAddress_street();
              String hausnummer = feature.getProperties().getAddress_housenumber();
              String plz = feature.getProperties().getAddress_postcode();
              String stadt = feature.getProperties().getAddress_city();
              String emergency = feature.getProperties().getEmergency();
              String rooms = feature.getProperties().getRooms();
              String beds = feature.getProperties().getBeds();
              String capacity = feature.getProperties().getCapacity();
              String wheelchair = feature.getProperties().getWheelchair();
              Double lat = feature.getGeometry().getCoordinates().get(1);
              Double lon = feature.getGeometry().getCoordinates().get(0);

              return new Krankenhaus(
                  name,
                  amenty,
                  healthcare,
                  operator,
                  telefonnummer,
                  webseite,
                  strasse,
                  hausnummer,
                  plz,
                  stadt,
                  emergency,
                  rooms,
                  beds,
                  capacity,
                  wheelchair,
                  lat,
                  lon,
                  Instant.now());
            })
        .collect(Collectors.toList());
  }
}
