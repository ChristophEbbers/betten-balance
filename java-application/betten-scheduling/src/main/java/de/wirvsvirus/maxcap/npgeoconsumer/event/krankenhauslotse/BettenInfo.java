package de.wirvsvirus.maxcap.npgeoconsumer.event.krankenhauslotse;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.List;

@Data
public class BettenInfo {

  List<KHResult> kh_results;

  @Data
  public static class KHResult {
    @JsonProperty("add_id_bundesland")
    String bundesland;

    @JsonProperty("name")
    String name;

    @JsonProperty("strasse")
    String strasse;

    @JsonProperty("plz")
    String plz;

    @JsonProperty("ort")
    String ort;

    @JsonProperty("anzahl_betten")
    String anzahlBetten;

    @JsonProperty("ap_aerzte_ohne_belegaerzte_anzahl_amb")
    String aerzteAmbulant;

    @JsonProperty("ap_aerzte_ohne_belegaerzte_anzahl_stat")
    String aerzteStationaer;

    @JsonProperty("pflegekraefte_gesundheitskrankenpfleger_amb")
    String krankenpflegerAmbulant;

    @JsonProperty("pflegekraefte_gesundheitskrankenpfleger_stat")
    String krankenpflegerStationaer;
  }
}
