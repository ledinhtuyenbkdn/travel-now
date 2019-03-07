package com.ledinhtuyenbkdn.travelnow.repositories;

import com.ledinhtuyenbkdn.travelnow.model.Province;
import org.springframework.data.neo4j.repository.Neo4jRepository;

public interface ProvinceRepository extends Neo4jRepository<Province, Long> {
}
