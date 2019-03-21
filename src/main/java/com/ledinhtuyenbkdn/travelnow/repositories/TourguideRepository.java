package com.ledinhtuyenbkdn.travelnow.repositories;

import com.ledinhtuyenbkdn.travelnow.model.Tourguide;
import org.springframework.data.neo4j.repository.Neo4jRepository;

public interface TourguideRepository extends Neo4jRepository<Tourguide, Long> {
}
