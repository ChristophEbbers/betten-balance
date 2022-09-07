package de.wirvsvirus.maxcap;

import java.time.Instant;
import org.springframework.data.mongodb.core.mapping.Document;

@Document
public record FaelleBundesland(
    String landId,
    String landFull,
    String bezeichnung,
    long landEinwohnerZahl,
    long fallzahl,
    Instant zeitpunktAktualisierung,
    Double faelleAud100000Einwohner,
    String shapeArea,
    String shapeLength,
    long death) {}
