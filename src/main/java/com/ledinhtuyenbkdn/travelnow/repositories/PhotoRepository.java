package com.ledinhtuyenbkdn.travelnow.repositories;

import com.ledinhtuyenbkdn.travelnow.model.Photo;
import org.springframework.data.neo4j.repository.Neo4jRepository;

public interface PhotoRepository extends Neo4jRepository<Photo, Long> {
}
