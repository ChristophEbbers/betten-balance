package de.wirvsvirus.maxcap;

import de.wirvsvirus.maxcap.event.krankenhauslotse.FaelleBundesland;
import de.wirvsvirus.maxcap.repository.FaelleBundeslandRepository;
import java.util.List;
import java.util.Objects;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Component
@Slf4j
@RequiredArgsConstructor
public class CallFaelleBundeslandApi {

  private final RestTemplate restTemplate;
  private final FaelleBundeslandRepository faelleEinwohnerBundeslandRepository;

  @Value("${npgeo.faelle.bundesland.uri}")
  private String npgeoFaelleBundeslandUri;

//  @Scheduled(initialDelayString = "PT1M")
  public void consumeNpgeoFaelleBundesland() {
    saveFaelleBundesland(restTemplate);
  }

  public void saveFaelleBundesland(@Qualifier("npgeoApiClientTemplate") RestTemplate restTemplate) {
    var faelleEinwohnerBundeslands =
        toFaelleBundeslandModel(
            Objects.requireNonNull(
                restTemplate.getForObject(npgeoFaelleBundeslandUri, FaelleBundesland.class)));

    faelleEinwohnerBundeslands.forEach(faelleEinwohnerBundeslandRepository::save);
    log.info("Saved {} Faelle pro Bundesland", faelleEinwohnerBundeslands.size());
  }

  private List<de.wirvsvirus.maxcap.FaelleBundesland> toFaelleBundeslandModel(
      FaelleBundesland apiNpgeoFaelle) {
    return apiNpgeoFaelle.getFeatures().stream()
        .map(
            feature -> {
              var landId = removeLeadingZero(feature.getProperties().getLanEwAgs());
              var landFull = feature.getProperties().getLanEwGen();
              var bezeichnung = feature.getProperties().getLanEwBez();
              var landEinwohnerZahl = feature.getProperties().getLanEwEwz();
              var fallzahl = feature.getProperties().getFallzahl();
              var zeitpunktAktualisierung = feature.getProperties().getZeitpunkt();
              var faelleAud100000Einwohner = feature.getProperties().getFaelle100000EW();
              var shapeArea = feature.getProperties().getShapeArea();
              var shapeLength = feature.getProperties().getShapeLength();
              var death = feature.getProperties().getDeath();

              return new de.wirvsvirus.maxcap.FaelleBundesland(
                  landId,
                  landFull,
                  bezeichnung,
                  landEinwohnerZahl,
                  fallzahl,
                  zeitpunktAktualisierung,
                  faelleAud100000Einwohner,
                  shapeArea,
                  shapeLength,
                  death);
            })
        .toList();
  }

  private static String removeLeadingZero(String landId) {
    if (Objects.equals(landId.substring(0, 1), "0")) {
      landId = landId.substring(1, 2);
    }
    return landId;
  }
}
