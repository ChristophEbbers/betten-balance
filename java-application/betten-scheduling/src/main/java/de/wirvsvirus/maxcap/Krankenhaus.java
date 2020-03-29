package de.wirvsvirus.maxcap;

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
  String telefonnummer;
  String webseite;
  String strasse;
  String hausnummer;
  String plz;
  String stadt;
  String emergency;
  @Indexed String rooms;
  @Indexed String beds;
  String capacity;
  String wheelchair;
  Double geometryX;
  Double geometryY;
  Instant zeitpunkt;
}
