package com.ledinhtuyenbkdn.travelnow.repositories;

import com.ledinhtuyenbkdn.travelnow.model.Place;
import org.springframework.data.neo4j.repository.Neo4jRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PlaceRepository extends Neo4jRepository<Place, Long> {
    Place findByNamePlace(String name);
}
