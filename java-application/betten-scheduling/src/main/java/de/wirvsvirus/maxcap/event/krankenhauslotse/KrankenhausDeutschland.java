package de.wirvsvirus.maxcap.event.krankenhauslotse;

import lombok.Data;

import java.util.List;

@Data
public class KrankenhausDeutschland {

  List<Feature> features;

  @Data
  public static class Feature {
    Properties properties;
    Geometry geometry;
  }

  @Data
  public static class Properties {
    String name;
    String amenity;
    String healthcare;
    String operator;
    String contact_phone;
    String contact_website;
    String address_street;
    String address_housenumber;
    String address_postcode;
    String address_city;
    String emergency;
    String rooms;
    String beds;
    String capacity;
    String wheelchair;
  }

  @Data
  public static class Geometry {
    List<Double> coordinates;
  }
}
