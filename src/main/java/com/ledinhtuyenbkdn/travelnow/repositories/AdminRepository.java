package com.ledinhtuyenbkdn.travelnow.repositories;

import com.ledinhtuyenbkdn.travelnow.model.Admin;
import org.springframework.data.neo4j.repository.Neo4jRepository;

public interface AdminRepository extends Neo4jRepository<Admin, Long> {
}
