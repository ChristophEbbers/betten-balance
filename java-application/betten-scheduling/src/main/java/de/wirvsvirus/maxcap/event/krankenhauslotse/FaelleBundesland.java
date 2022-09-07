package de.wirvsvirus.maxcap.event.krankenhauslotse;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.time.Instant;
import java.util.List;
import lombok.Data;

@Data
public class FaelleBundesland {
  @JsonProperty("features")
  List<Feature> features;

  @Data
  public static class Feature {
    @JsonProperty("properties")
    Properties properties;
  }

  @Data
  public static class Properties {
    @JsonProperty("LAN_ew_AGS")
    String lanEwAgs;

    @JsonProperty("LAN_ew_GEN")
    String lanEwGen;

    @JsonProperty("LAN_ew_BEZ")
    String lanEwBez;

    @JsonProperty("LAN_ew_EWZ")
    long lanEwEwz;

    @JsonProperty("Fallzahl")
    long fallzahl;

    @JsonProperty("Aktualisierung")
    Instant zeitpunkt;

    @JsonProperty("faelle_100000_EW")
    Double faelle100000EW;

    @JsonProperty("Shape__Area")
    String shapeArea;

    @JsonProperty("Shape__Length")
    String shapeLength;

    @JsonProperty("Death")
    long death;
  }
}
