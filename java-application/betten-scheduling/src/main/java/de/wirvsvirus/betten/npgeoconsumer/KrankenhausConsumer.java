package de.wirvsvirus.betten.npgeoconsumer;

import de.wirvsvirus.betten.Krankenhaus;
import de.wirvsvirus.betten.ResolveLongAndLatitude;
import de.wirvsvirus.betten.npgeoconsumer.event.ApiNpgeoKrankenhaus;
import de.wirvsvirus.betten.npgeoconsumer.event.StateAndCounty;
import de.wirvsvirus.betten.npgeoconsumer.repository.KrankenhausRepository;
import java.time.Instant;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.client.RestTemplate;

@Controller
@RequestMapping("/test")
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
  @GetMapping("/")
  public void consumeNpgeoKrankenhaeuser() {
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
              String phone = feature.getProperties().getContact_phone();
              String website = feature.getProperties().getContact_website();
              String street = feature.getProperties().getAddress_street();
              String housenumber = feature.getProperties().getAddress_housenumber();
              String postcode = feature.getProperties().getAddress_postcode();
              String city = feature.getProperties().getAddress_city();
              String emergency = feature.getProperties().getEmergency();
              String rooms = feature.getProperties().getRooms();
              String beds = feature.getProperties().getBeds();
              String capacity = feature.getProperties().getCapacity();
              String wheelchair = feature.getProperties().getWheelchair();
              Double lat = feature.getGeometry().getCoordinates().get(1);
              Double lon = feature.getGeometry().getCoordinates().get(0);


//              StateAndCounty stateAndCounty = resolveLongAndLatitude.getStateAndCounty(lat, lon);
//              String state = stateAndCounty.getAddress().getState();
//              String county = stateAndCounty.getAddress().getCounty();

              return new Krankenhaus(
                  name,
                  amenty,
                  healthcare,
                  operator,
                  phone,
                  website,
                  street,
                  housenumber,
                  postcode,
                  city,
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
