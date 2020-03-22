package de.wirvsvirus.betten;

import java.time.Instant;
import lombok.Value;
import org.springframework.data.mongodb.core.mapping.Document;

@Document("FaelleLandkreis")
@Value
public class FaelleLandkreis {

  String landId;
  String bundesland;
  String landkreis;
  String altersgruppe;
  String geschlecht;
  long anzahlFaelle;
  long todesFaelle;
  Instant meldeZeitpunkt;
  String landkreisId;
}
