package com.ledinhtuyenbkdn.travelnow.repository;

import com.ledinhtuyenbkdn.travelnow.model.Post;
import org.springframework.data.neo4j.annotation.Query;
import org.springframework.data.neo4j.repository.Neo4jRepository;

import java.util.List;

public interface PostRepository extends Neo4jRepository<Post, Long> {
    @Query("match (i1:Image)<-[r1:HAS_IMAGE]-(t:Tourist)<-[r2:POSTED_BY]-(p:Post)-[r3:HAS_IMAGE]->(i2:Image), (p:Post)-[r4:TAG_WITH]->(place:Place)-[r5:HAS_IMAGE]->(i3:Image) where ID(t) = {0} return r1,r2,r3,r4,r5,p,place,i1,i2,i3,t;")
    List<Post> findAllByTouristId(Long id);
}
