package de.wirvsvirus.maxcap.npgeoconsumer.event;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class StateAndCounty {

  Address address;

  @Data
  public static class Address {
    @JsonProperty("county")
    String county;

    @JsonProperty("state")
    String state;
  }
}
