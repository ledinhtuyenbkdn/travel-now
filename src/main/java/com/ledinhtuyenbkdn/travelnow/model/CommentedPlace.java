package com.ledinhtuyenbkdn.travelnow.model;

import org.neo4j.ogm.annotation.*;

import javax.validation.constraints.NotEmpty;
import java.time.LocalDateTime;

@RelationshipEntity(type = "COMMENTED_PLACE")
public class CommentedPlace {
    @Id
    @GeneratedValue
    private Long id;

    @NotEmpty
    private String content;

    private LocalDateTime createdAt;
    @StartNode
    private Tourist tourist;
    @EndNode
    private Place place;

    public CommentedPlace() {
    }

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

    public Place getPlace() {
        return place;
    }

    public void setPlace(Place place) {
        this.place = place;
    }
}
