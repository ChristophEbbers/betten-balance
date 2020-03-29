package de.wirvsvirus.maxcap.npgeoconsumer.repository;

import de.wirvsvirus.maxcap.Krankenhaus;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface KrankenhausRepository extends MongoRepository<Krankenhaus, String> {}
