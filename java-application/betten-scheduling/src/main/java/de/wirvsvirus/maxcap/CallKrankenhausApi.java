package de.wirvsvirus.maxcap;

import com.fasterxml.jackson.databind.ObjectMapper;
import de.wirvsvirus.maxcap.event.krankenhauslotse.BettenInfo;
import de.wirvsvirus.maxcap.event.krankenhauslotse.KrankenhausDeutschland;
import de.wirvsvirus.maxcap.repository.BettenRepository;
import de.wirvsvirus.maxcap.repository.KrankenhausRepository;
import java.io.File;
import java.io.IOException;
import java.time.Instant;
import java.util.List;
import java.util.Objects;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.client.RestTemplate;

@Controller
@RequestMapping("/maxcap")
@Component
@Slf4j
@RequiredArgsConstructor
public class CallKrankenhausApi {

  private final RestTemplate restTemplate;
  private final KrankenhausRepository krankenhausRepository;
  private final BettenRepository bettenRepository;
  private final ObjectMapper objectMapper;

  @Value("${npgeo.krankenhaus.uri}")
  private String npgeoKrankenhausBaseUri;

  @Value("${krankenhauslotse.uri}")
  private String krankenhauslotseBaseUri;

//  @Scheduled(initialDelayString = "PT10M")
  @GetMapping("/krankenhaus")
  public void consumeNpgeoKrankenhaeuser() {
    log.info("Consume Krankenhaus from {}", npgeoKrankenhausBaseUri);
    saveKrankenhaeuserFromApi(restTemplate);
  }

  @GetMapping("/betten")
  public ResponseEntity<Void> consumeBetten() {
    try {
      var bettenInfo = objectMapper.readValue(new File("temp.json"), BettenInfo.class);
      var betten = toBettenDeutschland(bettenInfo);
      betten.forEach(bettenRepository::save);
      log.info("{}", betten.size());
    } catch (IOException e) {
      e.printStackTrace();
    }

    return new ResponseEntity<>(HttpStatus.OK);
  }

  private void saveKrankenhaeuserFromApi(
      @Qualifier("npgeoApiClientTemplate") RestTemplate restTemplate) {
    var krankenhaus =
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
              var bundesland = khResult.getBundesland();
              var name = khResult.getName();
              var strasse = khResult.getStrasse();
              var plz = khResult.getPlz();
              var ort = khResult.getOrt();
              var anzahlBetten = khResult.getAnzahlBetten();
              var aerzteAmbulant = khResult.getAerzteAmbulant();
              var aerzteStationaer = khResult.getAerzteStationaer();
              var krankenpflegerAmbulant = khResult.getKrankenpflegerAmbulant();
              var krankenpflegerStationaer = khResult.getKrankenpflegerStationaer();

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
        .toList();
  }

  private List<Krankenhaus> toKrankenhausModel(KrankenhausDeutschland apiNpgeoKrankenhaus) {
    return apiNpgeoKrankenhaus.getFeatures().stream()
        .map(
            feature -> {
              var name = feature.getProperties().getName();
              var amenty = feature.getProperties().getAmenity();
              var healthcare = feature.getProperties().getHealthcare();
              var operator = feature.getProperties().getOperator();
              var telefonnummer = feature.getProperties().getContact_phone();
              var webseite = feature.getProperties().getContact_website();
              var strasse = feature.getProperties().getAddress_street();
              var hausnummer = feature.getProperties().getAddress_housenumber();
              var plz = feature.getProperties().getAddress_postcode();
              var stadt = feature.getProperties().getAddress_city();
              var emergency = feature.getProperties().getEmergency();
              var rooms = feature.getProperties().getRooms();
              var beds = feature.getProperties().getBeds();
              var capacity = feature.getProperties().getCapacity();
              var wheelchair = feature.getProperties().getWheelchair();
              var lat = feature.getGeometry().getCoordinates().get(1);
              var lon = feature.getGeometry().getCoordinates().get(0);

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
        .toList();
  }
}
