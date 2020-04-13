package de.wirvsvirus.maxcap.npgeoconsumer;

import com.fasterxml.jackson.databind.ObjectMapper;
import de.wirvsvirus.maxcap.Krankenhaus;
import de.wirvsvirus.maxcap.npgeoconsumer.event.krankenhauslotse.BettenInfo;
import de.wirvsvirus.maxcap.npgeoconsumer.event.npgeo.KrankenhausDeutschland;
import de.wirvsvirus.maxcap.npgeoconsumer.repository.BettenRepository;
import de.wirvsvirus.maxcap.npgeoconsumer.repository.KrankenhausRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.client.RestTemplate;

import java.io.File;
import java.io.IOException;
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
  private final BettenRepository bettenRepository;
  private final ObjectMapper objectMapper;

  @Value("${npgeo.krankenhaus.uri}")
  private String npgeoKrankenhausBaseUri;

  @Value("${krankenhauslotse.uri}")
  private String krankenhauslotseBaseUri;

  // @Scheduled(initialDelayString = "PT1M", fixedDelayString = "10000000")
  @GetMapping("/krankenhaus")
  public void consumeNpgeoKrankenhaeuser() {
    log.info("Consume Krankenhaus from {}", npgeoKrankenhausBaseUri);
    saveKrankenhaeuserFromApi(restTemplate);
  }

  @GetMapping("/betten")
  public ResponseEntity<Void> consumeBetten() {
    try {
      BettenInfo bettenInfo = objectMapper.readValue(new File("temp.json"), BettenInfo.class);
      List<BettenDeutschland> betten = toBettenDeutschland(bettenInfo);
      //betten.forEach(bettenRepository::save);
    log.info("{}", betten.size());
    } catch (IOException e) {
      e.printStackTrace();
    }

    return new ResponseEntity<>(HttpStatus.OK);
  }

  private void saveKrankenhaeuserFromApi(
      @Qualifier("npgeoApiClientTemplate") RestTemplate restTemplate) {
    List<Krankenhaus> krankenhaus =
        toKrankenhausModel(
            Objects.requireNonNull(
                restTemplate.getForObject(npgeoKrankenhausBaseUri, KrankenhausDeutschland.class)));
    krankenhaus.forEach(krankenhausRepository::save);
    log.info("Saved {} Krankenhäuser", krankenhaus.size());
  }

  private List<BettenDeutschland> toBettenDeutschland(BettenInfo betten) {
    return betten.getKh_results().stream()
        .map(
            khResult -> {
              String bundesland = khResult.getBundesland();
              String name = khResult.getName();
              String strasse = khResult.getStrasse();
              String plz = khResult.getPlz();
              String ort = khResult.getOrt();
              String anzahlBetten = khResult.getAnzahlBetten();
              String aerzteAmbulant = khResult.getAerzteAmbulant();
              String aerzteStationaer = khResult.getAerzteStationaer();
              String krankenpflegerAmbulant = khResult.getKrankenpflegerAmbulant();
              String krankenpflegerStationaer = khResult.getKrankenpflegerStationaer();

              return new BettenDeutschland(
                  bundesland,
                  name,
                  strasse,
                  plz,
                  ort,
                  anzahlBetten,
                  aerzteAmbulant,
                  aerzteStationaer,
                  krankenpflegerAmbulant,
                  krankenpflegerStationaer);
            })
        .collect(Collectors.toList());
  }

  private List<Krankenhaus> toKrankenhausModel(KrankenhausDeutschland apiNpgeoKrankenhaus) {
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
