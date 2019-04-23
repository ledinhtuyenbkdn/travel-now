package com.ledinhtuyenbkdn.travelnow.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.neo4j.ogm.annotation.*;

import javax.validation.constraints.NotEmpty;
import java.time.LocalDateTime;

@RelationshipEntity(type = "COMMENTED_POST")
public class CommentedPost {
    @Id
    @GeneratedValue
    private Long id;

    @NotEmpty
    private String content;

    private LocalDateTime createdAt;
    @StartNode
    private Tourist tourist;
    @EndNode
    @JsonIgnore
    private Post post;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
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
