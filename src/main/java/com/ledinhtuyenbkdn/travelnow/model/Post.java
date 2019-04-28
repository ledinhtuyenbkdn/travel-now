package com.ledinhtuyenbkdn.travelnow.model;

import org.neo4j.ogm.annotation.GeneratedValue;
import org.neo4j.ogm.annotation.Id;
import org.neo4j.ogm.annotation.NodeEntity;
import org.neo4j.ogm.annotation.Relationship;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@NodeEntity
public class Post {

    @Id
    @GeneratedValue
    private Long id;
    private String content;
    private LocalDateTime createdAt;
    @Relationship(type = "HAS_IMAGE", direction = Relationship.OUTGOING)
    private Set<Image> images = new HashSet<>();
    @Relationship(type = "TAG_WITH", direction = Relationship.OUTGOING)
    private Place place;
    @Relationship(type = "POSTED_BY", direction = Relationship.OUTGOING)
    private Tourist tourist;

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

    public Set<Image> getImages() {
        return images;
    }

    public void setImages(Set<Image> images) {
        this.images = images;
    }

    public Place getPlace() {
        return place;
    }

    public void setPlace(Place place) {
        this.place = place;
    }

    public Tourist getTourist() {
        return tourist;
    }

    public void setTourist(Tourist tourist) {
        this.tourist = tourist;
    }
}
