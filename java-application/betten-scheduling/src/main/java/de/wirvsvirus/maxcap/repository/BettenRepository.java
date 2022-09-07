package de.wirvsvirus.maxcap.repository;

import de.wirvsvirus.maxcap.BettenDeutschland;
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
