package com.ledinhtuyenbkdn.travelnow.model;

import org.neo4j.ogm.annotation.*;

@RelationshipEntity(type = "LIKED_POST")
public class LikedPost {
    @Id
    @GeneratedValue
    private Long id;
    @StartNode
    private Tourist tourist;
    @EndNode
    private Post post;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Tourist getTourist() {
        return tourist;
    }

    public void setTourist(Tourist tourist) {
        this.tourist = tourist;
    }

    public Post getPost() {
        return post;
    }

    public void setPost(Post post) {
        this.post = post;
    }
}
