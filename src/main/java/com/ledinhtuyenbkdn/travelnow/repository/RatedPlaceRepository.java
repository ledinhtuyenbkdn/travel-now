package com.ledinhtuyenbkdn.travelnow.repository;

import com.ledinhtuyenbkdn.travelnow.model.RatedPlace;
import org.springframework.data.neo4j.annotation.Query;
import org.springframework.data.neo4j.repository.Neo4jRepository;

import java.util.List;
import java.util.Optional;

public interface RatedPlaceRepository extends Neo4jRepository<RatedPlace, Long> {
    @Query("match (i:Image)<-[r1:HAS_IMAGE]-(a:Tourist)-[r2:RATED_PLACE]->(b:Place) where ID(b) = {0} return i, a, r1, r2, b;")
    List<RatedPlace> findByPlaceId(Long id);

    @Query("match (i:Image)<-[r1:HAS_IMAGE]-(a:Tourist)-[r2:RATED_PLACE]->(b:Place) where ID(b) = {0} and ID(a) = {1} return i, a, r1, r2, b;")
    Optional<RatedPlace> findByPlaceIdAndTouristId(Long idPlace, Long idTourist);
}
