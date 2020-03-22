package de.wirvsvirus.betten;

import java.time.Instant;
import lombok.Value;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

@Document("Krankenhaus")
@Value
public class Krankenhaus {

  @Indexed String name;
  String amenity;
  @Indexed String healthcare;
  String operator;
  String contact_phone;
  String contact_website;
  String address_street;
  String address_housenumber;
  String address_postcode;
  String address_city;
  String emergency;
  @Indexed String rooms;
  @Indexed String beds;
  String capacity;
  String wheelchair;
  Double geometryX;
  Double geometryY;
  Instant zeitpunkt;
}
