package de.wirvsvirus.maxcap.npgeoconsumer.event.npgeo;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.time.Instant;
import java.util.List;
import lombok.Data;

@Data
public class FaelleLandkreis {
  @JsonProperty("features")
  List<Feature> features;

  @Data
  public static class Feature {
    @JsonProperty("properties")
    Properties properties;
  }

  @Data
  public static class Properties {
    @JsonProperty("IdBundesland")
    String landId;

    @JsonProperty("Bundesland")
    String bundesland;

    @JsonProperty("Landkreis")
    String landkreis;

    @JsonProperty("Altersgruppe")
    String altersgruppe;

    @JsonProperty("Geschlecht")
    String geschlecht;

    @JsonProperty("AnzahlFall")
    long faelle;

    @JsonProperty("AnzahlTodesfall")
    long todesfaelle;

    @JsonProperty("Meldedatum")
    Instant meldeZeitpunkt;

    @JsonProperty("IdLandkreis")
    String landkreisId;
  }
}
