package de.wirvsvirus.maxcap.npgeoconsumer.repository;

import de.wirvsvirus.maxcap.npgeoconsumer.BettenDeutschland;
import lombok.RequiredArgsConstructor;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class BettenRepository {

  private final MongoTemplate mongoTemplate;

  public void save(BettenDeutschland bettenDeutschland) {
    mongoTemplate.save(bettenDeutschland);
  }
}
