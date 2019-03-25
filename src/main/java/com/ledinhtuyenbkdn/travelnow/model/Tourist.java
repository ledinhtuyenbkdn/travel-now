package com.ledinhtuyenbkdn.travelnow.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.neo4j.ogm.annotation.NodeEntity;
import org.neo4j.ogm.annotation.Relationship;

import java.util.Set;

@NodeEntity
public class Tourist extends User {
    private String nationality;
    private String avatar;

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

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
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
}
