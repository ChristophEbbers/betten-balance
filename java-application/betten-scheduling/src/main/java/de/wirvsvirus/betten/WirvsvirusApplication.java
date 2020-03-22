package de.wirvsvirus.betten;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@EnableScheduling
@SpringBootApplication
public class WirvsvirusApplication {

  public static void main(String[] args) {
    SpringApplication.run(WirvsvirusApplication.class, args);
  }
}
