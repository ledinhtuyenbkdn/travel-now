package com.ledinhtuyenbkdn.travelnow.repository;

import com.ledinhtuyenbkdn.travelnow.model.Follow;
import org.springframework.data.neo4j.annotation.Query;
import org.springframework.data.neo4j.repository.Neo4jRepository;

import java.util.List;
import java.util.Optional;

public interface FollowRepository extends Neo4jRepository<Follow, Long> {

    @Query("match (follower:Tourist)-[r:IS_FOLLOWING]->(followee:Tourist) where ID(follower)={0} and ID(followee)={1} return follower, r, followee;")
    Optional<Follow> findByFollowerIdAndFolloweeId(Long followerId, Long followeeId);

    @Query("match (follower:Tourist)-[r:IS_FOLLOWING]->(followee:Tourist) where ID(follower)={0} return follower, r, followee;")
    List<Follow> findByFollowerId(Long id);

    @Query("match (follower:Tourist)-[r:IS_FOLLOWING]->(followee:Tourist) where ID(followee)={0} return follower, r, followee;")
    List<Follow> findByFolloweeId(Long id);
}
