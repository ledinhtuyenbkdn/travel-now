package com.ledinhtuyenbkdn.travelnow.repositories;

import com.ledinhtuyenbkdn.travelnow.model.Tourist;
import org.springframework.data.neo4j.repository.Neo4jRepository;

public interface TouristRepository extends Neo4jRepository<Tourist, Long> {
    Iterable<Tourist> findByUserName(String userName);
}
