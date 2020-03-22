package de.wirvsvirus.betten.npgeoconsumer;

import de.wirvsvirus.betten.FaelleBundesland;
import de.wirvsvirus.betten.npgeoconsumer.event.ApiNpgeoFaelleBundesland;
import de.wirvsvirus.betten.npgeoconsumer.repository.FaelleBundeslandRepository;
import java.time.Instant;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Component
@Slf4j
@RequiredArgsConstructor
public class FaelleBundeslandConsumer {

  private final RestTemplate restTemplate;
  private final FaelleBundeslandRepository faelleEinwohnerBundeslandRepository;

  @Value("${npgeo.faelle.bundesland.uri}")
  private String npgeoFaelleBundeslandUri;

  // @Scheduled(initialDelayString = "PT1M", fixedDelayString = "10000000")
  public void consumeNpgeoFaelleBundesland() {
    saveFaelleBundesland(restTemplate);
  }

  public void saveFaelleBundesland(@Qualifier("npgeoApiClientTemplate") RestTemplate restTemplate) {
    List<FaelleBundesland> faelleEinwohnerBundeslands =
        toFaelleBundeslandModel(
            Objects.requireNonNull(
                restTemplate.getForObject(
                    npgeoFaelleBundeslandUri, ApiNpgeoFaelleBundesland.class)));

    faelleEinwohnerBundeslands.forEach(faelleEinwohnerBundeslandRepository::save);
    log.info("Saved {} Faelle pro Bundesland", faelleEinwohnerBundeslands.size());
  }

  private List<FaelleBundesland> toFaelleBundeslandModel(ApiNpgeoFaelleBundesland apiNpgeoFaelle) {
    return apiNpgeoFaelle.getFeatures().stream()
        .map(
            feature -> {
              String landId = feature.getProperties().getLanEwAgs();
              landId = removeLeadingZero(landId);
              String landFull = feature.getProperties().getLanEwGen();
              String bezeichnung = feature.getProperties().getLanEwBez();
              long landEinwohnerZahl = feature.getProperties().getLanEwEwz();
              long fallzahl = feature.getProperties().getFallzahl();
              Instant zeitpunktAktualisierung = feature.getProperties().getZeitpunkt();
              Double faelleAud100000Einwohner = feature.getProperties().getFaelle100000EW();
              String shapeArea = feature.getProperties().getShapeArea();
              String shapeLength = feature.getProperties().getShapeLength();
              long death = feature.getProperties().getDeath();

              return new FaelleBundesland(
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
        .collect(Collectors.toList());
  }

  private static String removeLeadingZero(String landId) {
    if (Objects.equals(landId.substring(0, 1), "0")) {
      landId = landId.substring(1, 2);
    }
    return landId;
  }
}
