package de.wirvsvirus.maxcap.npgeoconsumer.repository;

import de.wirvsvirus.maxcap.npgeoconsumer.BettenDeutschland;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface BettenRepository extends MongoRepository<BettenDeutschland, String> {}
