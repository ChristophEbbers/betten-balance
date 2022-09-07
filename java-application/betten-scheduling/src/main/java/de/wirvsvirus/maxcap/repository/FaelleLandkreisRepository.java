package de.wirvsvirus.maxcap.repository;

import de.wirvsvirus.maxcap.FaelleLandkreis;
import lombok.RequiredArgsConstructor;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class FaelleLandkreisRepository {

  private final MongoTemplate mongoTemplate;

  public void save(FaelleLandkreis faelleLandkreis) {
    mongoTemplate.save(faelleLandkreis);
  }
}
