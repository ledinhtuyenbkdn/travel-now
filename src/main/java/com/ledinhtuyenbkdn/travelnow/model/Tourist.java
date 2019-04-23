package com.ledinhtuyenbkdn.travelnow.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.neo4j.ogm.annotation.NodeEntity;
import org.neo4j.ogm.annotation.Relationship;

import java.util.Set;

@NodeEntity
public class Tourist extends User {
    private String nationality;

    @Relationship(type = "HAS_IMAGE", direction = Relationship.OUTGOING)
    private Image image;

    @JsonIgnore
    @Relationship(type = "HAS_POST", direction = Relationship.OUTGOING)
    private Set<Post> posts;

    @JsonIgnore
    @Relationship(type = "RATED_PLACE", direction = Relationship.OUTGOING)
    private Set<Place> ratedPlaces;

    @JsonIgnore
    @Relationship(type = "COMMENTED_PLACE", direction = Relationship.OUTGOING)
    private Set<Place> commentedPlaces;

    public Tourist() {
    }

    public String getNationality() {
        return nationality;
    }

    public void setNationality(String nationality) {
        this.nationality = nationality;
    }

    public Image getImage() {
        return image;
    }

    public void setImage(Image image) {
        this.image = image;
    }

    public Set<Place> getRatedPlaces() {
        return ratedPlaces;
    }

    public void setRatedPlaces(Set<Place> ratedPlaces) {
        this.ratedPlaces = ratedPlaces;
    }

    public Set<Place> getCommentedPlaces() {
        return commentedPlaces;
    }

    public void setCommentedPlaces(Set<Place> commentedPlaces) {
        this.commentedPlaces = commentedPlaces;
    }

    public Set<Post> getPosts() {
        return posts;
    }

    public void setPosts(Set<Post> posts) {
        this.posts = posts;
    }
}
