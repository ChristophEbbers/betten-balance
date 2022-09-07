package de.wirvsvirus.maxcap.repository;

import de.wirvsvirus.maxcap.FaelleBundesland;
import lombok.RequiredArgsConstructor;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class FaelleBundeslandRepository {

  private final MongoTemplate mongoTemplate;

  public void save(FaelleBundesland faelleBundesland) {
    mongoTemplate.save(faelleBundesland);
  }
}
