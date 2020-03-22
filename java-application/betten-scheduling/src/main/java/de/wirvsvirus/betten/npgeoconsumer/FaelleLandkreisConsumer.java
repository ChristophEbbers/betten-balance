package de.wirvsvirus.betten.npgeoconsumer;

import de.wirvsvirus.betten.FaelleLandkreis;
import de.wirvsvirus.betten.npgeoconsumer.event.ApiNpgeoFaelleLandkreis;
import de.wirvsvirus.betten.npgeoconsumer.repository.FaelleLandkreisRepository;
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
public class FaelleLandkreisConsumer {

  private final RestTemplate restTemplate;
  private final FaelleLandkreisRepository faelleLandkreisRepository;

  @Value("${npgeo.faelle.landkreis.uri}")
  private String npgeoFaelleLandkreisUri;

  // @Scheduled(initialDelayString = "PT1M", fixedDelayString = "10000000")
  public void consumeNpgeoFaelleBundesland() {
    saveFaelleLandkreis(restTemplate);
  }

  public void saveFaelleLandkreis(@Qualifier("npgeoApiClientTemplate") RestTemplate restTemplate) {
    List<FaelleLandkreis> faelleLankreis =
        toFaelleBundeslandModel(
            Objects.requireNonNull(
                restTemplate.getForObject(npgeoFaelleLandkreisUri, ApiNpgeoFaelleLandkreis.class)));

    faelleLankreis.forEach(faelleLandkreisRepository::save);
    log.info("Saved {} Faelle pro Landkreis", faelleLankreis.size());
  }

  private List<FaelleLandkreis> toFaelleBundeslandModel(ApiNpgeoFaelleLandkreis apiNpgeoFaelle) {
    return apiNpgeoFaelle.getFeatures().stream()
        .map(
            feature -> {
              String landId = feature.getProperties().getLandId();
              String bundesland = feature.getProperties().getBundesland();
              String landkreis = feature.getProperties().getLandkreis();
              String altersGruppe = feature.getProperties().getAltersgruppe();
              altersGruppe = altersGruppe.replaceAll("A", "");
              String geschlecht = feature.getProperties().getGeschlecht();
              long anzahlFaelle = feature.getProperties().getFaelle();
              long anzahlTodesFaelle = feature.getProperties().getTodesfaelle();
              Instant meldeZeitpunkt = feature.getProperties().getMeldeZeitpunkt();
              String landkreisId = feature.getProperties().getLandkreisId();

              return new FaelleLandkreis(
                  landId,
                  bundesland,
                  landkreis,
                  altersGruppe,
                  geschlecht,
                  anzahlFaelle,
                  anzahlTodesFaelle,
                  meldeZeitpunkt,
                  landkreisId);
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
