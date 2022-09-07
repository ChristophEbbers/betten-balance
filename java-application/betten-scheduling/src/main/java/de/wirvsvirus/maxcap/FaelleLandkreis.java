package de.wirvsvirus.maxcap;

import java.time.Instant;
import org.springframework.data.mongodb.core.mapping.Document;

@Document
public record FaelleLandkreis(
    String landId,
    String bundesland,
    String landkreis,
    String altersgruppe,
    String geschlecht,
    long anzahlFaelle,
    long todesFaelle,
    Instant meldeZeitpunkt,
    String landkreisId) {}
