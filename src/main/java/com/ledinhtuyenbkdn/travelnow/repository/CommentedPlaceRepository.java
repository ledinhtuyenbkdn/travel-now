package com.ledinhtuyenbkdn.travelnow.repository;

import com.ledinhtuyenbkdn.travelnow.model.CommentedPlace;
import org.springframework.data.neo4j.annotation.Query;
import org.springframework.data.neo4j.repository.Neo4jRepository;

import java.util.List;

public interface CommentedPlaceRepository extends Neo4jRepository<CommentedPlace, Long> {
    @Query("match (i:Image)<-[r1:HAS_IMAGE]-(a:Tourist)-[r2:COMMENTED_PLACE]->(b:Place) where ID(b) = {0} return i, a, r1, r2, b;")
    List<CommentedPlace> findByPlaceId(Long id);
}
