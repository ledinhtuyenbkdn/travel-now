package com.ledinhtuyenbkdn.travelnow.repository;

import com.ledinhtuyenbkdn.travelnow.model.LikedPost;
import org.springframework.data.neo4j.annotation.Query;
import org.springframework.data.neo4j.repository.Neo4jRepository;

import java.util.List;

public interface LikedPostRepository extends Neo4jRepository<LikedPost, Long> {
    @Query("match (i:Image)<-[r1:HAS_IMAGE]-(a:Tourist)-[r2:LIKED_POST]->(b:Post) where ID(b) = {0} return i, a, r1, r2, b;")
    List<LikedPost> findByPostId(Long id);

    @Query("match (i:Image)<-[r1:HAS_IMAGE]-(a:Tourist)-[r2:LIKED_POST]->(b:Post) where ID(b) = {0} and ID(a) = {1} return i, a, r1, r2, b;")
    List<LikedPost> findByPostIdAndTouristId(Long idPost, Long idTourist);
}
