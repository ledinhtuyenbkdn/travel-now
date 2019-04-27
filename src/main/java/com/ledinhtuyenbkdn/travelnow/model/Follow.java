package com.ledinhtuyenbkdn.travelnow.model;

import org.neo4j.ogm.annotation.*;

@RelationshipEntity(type = "IS_FOLLOWING")
public class Follow {

    @Id
    @GeneratedValue
    private Long id;
    @StartNode
    private Tourist follower;
    @EndNode
    private Tourist followee;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Tourist getFollower() {
        return follower;
    }

    public void setFollower(Tourist follower) {
        this.follower = follower;
    }

    public Tourist getFollowee() {
        return followee;
    }

    public void setFollowee(Tourist followee) {
        this.followee = followee;
    }
}
