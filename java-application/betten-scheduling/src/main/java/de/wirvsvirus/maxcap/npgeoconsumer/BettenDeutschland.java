package de.wirvsvirus.maxcap.npgeoconsumer;

import org.springframework.data.mongodb.core.mapping.Document;

@Document
public record BettenDeutschland(
    String bundesland,
    String name,
    String strasse,
    String plz,
    String ort,
    String anzahlBetten,
    String aerzteAmbulant,
    String aerzteStationaer,
    String krankenpflegerAmbulant,
    String krankenpflegerStationaer) {}
