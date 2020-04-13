package de.wirvsvirus.maxcap.npgeoconsumer;

import lombok.Value;
import org.springframework.data.mongodb.core.mapping.Document;

@Document("Betten")
@Value
public class BettenDeutschland {

  String bundesland;
  String name;
  String strasse;
  String plz;
  String ort;
  String anzahlBetten;
  String aerzteAmbulant;
  String aerzteStationaer;
  String krankenpflegerAmbulant;
  String krankenpflegerStationaer;
}
