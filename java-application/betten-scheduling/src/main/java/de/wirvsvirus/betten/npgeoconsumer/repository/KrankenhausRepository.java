package de.wirvsvirus.betten.npgeoconsumer.repository;

import de.wirvsvirus.betten.Krankenhaus;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface KrankenhausRepository extends MongoRepository<Krankenhaus, String> {}
