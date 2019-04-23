package com.ledinhtuyenbkdn.travelnow.repository;

import com.ledinhtuyenbkdn.travelnow.model.Post;
import org.springframework.data.neo4j.repository.Neo4jRepository;

public interface PostRepository extends Neo4jRepository<Post, Long> {
}
