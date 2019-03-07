package com.ledinhtuyenbkdn.travelnow.model;

import org.neo4j.ogm.annotation.EndNode;
import org.neo4j.ogm.annotation.RelationshipEntity;
import org.neo4j.ogm.annotation.StartNode;

@RelationshipEntity(type = "COMMENTED_TOURGUIDE")
public class CommentedTourguide {
    private Long id;
    private String content;
    private String createdDateTime;
    @StartNode
    private Tourist tourist;
    @EndNode
    private Tourguide tourguide;

    public CommentedTourguide() {
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

    public Tourguide getTourguide() {
        return tourguide;
    }

    public void setTourguide(Tourguide tourguide) {
        this.tourguide = tourguide;
    }
}
