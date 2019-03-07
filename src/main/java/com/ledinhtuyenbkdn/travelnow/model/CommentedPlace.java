package com.ledinhtuyenbkdn.travelnow.model;

import org.neo4j.ogm.annotation.*;

@RelationshipEntity(type = "COMMENTED_PLACE")
public class CommentedPlace {
    @Id
    @GeneratedValue
    private Long id;
    private String content;
    private String createdDateTime;
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

    public String getCreatedDateTime() {
        return createdDateTime;
    }

    public void setCreatedDateTime(String createdDateTime) {
        this.createdDateTime = createdDateTime;
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
