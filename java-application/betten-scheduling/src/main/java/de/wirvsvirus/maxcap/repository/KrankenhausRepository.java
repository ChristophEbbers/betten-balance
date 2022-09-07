package de.wirvsvirus.maxcap.repository;

import de.wirvsvirus.maxcap.Krankenhaus;
import lombok.RequiredArgsConstructor;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class KrankenhausRepository {

  private final MongoTemplate mongoTemplate;

  public void save(Krankenhaus krankenhaus) {
    mongoTemplate.save(krankenhaus);
  }
}
