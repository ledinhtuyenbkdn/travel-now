package com.ledinhtuyenbkdn.travelnow.repository;

import com.ledinhtuyenbkdn.travelnow.model.CommentedPost;
import org.springframework.data.neo4j.annotation.Query;
import org.springframework.data.neo4j.repository.Neo4jRepository;

import java.util.List;

public interface CommentedPostRepository extends Neo4jRepository<CommentedPost, Long> {
    @Query("match (i:Image)<-[r1:HAS_IMAGE]-(a:Tourist)-[r2:COMMENTED_POST]->(b:Post) where ID(b) = {0} return i, a, r1, r2, b;")
    List<CommentedPost> findByPostId(Long id);
}
