package de.wirvsvirus.maxcap;

import de.wirvsvirus.maxcap.event.krankenhauslotse.FaelleLandkreis;
import de.wirvsvirus.maxcap.repository.FaelleLandkreisRepository;
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
public class CallFaelleLandkreisApi {

  private final RestTemplate restTemplate;
  private final FaelleLandkreisRepository faelleLandkreisRepository;

  @Value("${npgeo.faelle.landkreis.uri}")
  private String npgeoFaelleLandkreisUri;

//   @Scheduled(initialDelayString = "PT5M")
  public void consumeNpgeoFaelleBundesland() {
    saveFaelleLandkreis(restTemplate);
  }

  public void saveFaelleLandkreis(@Qualifier("npgeoApiClientTemplate") RestTemplate restTemplate) {
    var faelleLankreis =
        toFaelleBundeslandModel(
            Objects.requireNonNull(
                restTemplate.getForObject(npgeoFaelleLandkreisUri, FaelleLandkreis.class)));

    faelleLankreis.forEach(faelleLandkreisRepository::save);
    log.info("Saved {} Faelle pro Landkreis", faelleLankreis.size());
  }

  private List<de.wirvsvirus.maxcap.FaelleLandkreis> toFaelleBundeslandModel(
      FaelleLandkreis apiNpgeoFaelle) {
    return apiNpgeoFaelle.getFeatures().stream()
        .map(
            feature -> {
              var landId = feature.getProperties().getLandId();
              var bundesland = feature.getProperties().getBundesland();
              var landkreis = feature.getProperties().getLandkreis();
              var altersGruppe = feature.getProperties().getAltersgruppe().replaceAll("A", "");
              var geschlecht = feature.getProperties().getGeschlecht();
              var anzahlFaelle = feature.getProperties().getFaelle();
              var anzahlTodesFaelle = feature.getProperties().getTodesfaelle();
              var meldeZeitpunkt = feature.getProperties().getMeldeZeitpunkt();
              var landkreisId = feature.getProperties().getLandkreisId();

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
        .toList();
  }
}
