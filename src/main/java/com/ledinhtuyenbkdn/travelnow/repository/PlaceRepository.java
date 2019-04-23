package com.ledinhtuyenbkdn.travelnow.repository;

import com.ledinhtuyenbkdn.travelnow.model.Place;
import org.springframework.data.neo4j.repository.Neo4jRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PlaceRepository extends Neo4jRepository<Place, Long> {
    Place findByNamePlace(String name);

    List<Place> findAllByNamePlaceContains(String s);
}
