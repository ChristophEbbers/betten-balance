package de.wirvsvirus.maxcap.npgeoconsumer;

import de.wirvsvirus.maxcap.npgeoconsumer.event.npgeo.FaelleLandkreis;
import de.wirvsvirus.maxcap.npgeoconsumer.repository.FaelleLandkreisRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.time.Instant;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

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
    List<de.wirvsvirus.maxcap.FaelleLandkreis> faelleLankreis =
        toFaelleBundeslandModel(
            Objects.requireNonNull(
                restTemplate.getForObject(npgeoFaelleLandkreisUri, FaelleLandkreis.class)));

    faelleLankreis.forEach(faelleLandkreisRepository::save);
    log.info("Saved {} Faelle pro Landkreis", faelleLankreis.size());
  }

  private List<de.wirvsvirus.maxcap.FaelleLandkreis> toFaelleBundeslandModel(FaelleLandkreis apiNpgeoFaelle) {
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

              return new de.wirvsvirus.maxcap.FaelleLandkreis(
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
}
