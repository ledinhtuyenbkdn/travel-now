package com.ledinhtuyenbkdn.travelnow.model;

import org.neo4j.ogm.annotation.NodeEntity;
import org.neo4j.ogm.annotation.Relationship;

import java.util.Set;

@NodeEntity
public class Tourist extends User {
    private String nationality;
    private String avatar;
    @Relationship(type = "RATED_PLACE", direction = Relationship.OUTGOING)
    private Set<Place> ratedPlaces;
    @Relationship(type = "RATED_TOURGUIDE", direction = Relationship.OUTGOING)
    private Set<Tourguide> ratedTourguides;
    @Relationship(type = "COMMENTED_PLACE", direction = Relationship.OUTGOING)
    private Set<Place> commentedPlaces;
    @Relationship(type = "COMMENTED_TOURGUIDE", direction = Relationship.OUTGOING)
    private Set<Tourguide> commentedTourguides;

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

    public Set<Tourguide> getRatedTourguides() {
        return ratedTourguides;
    }

    public void setRatedTourguides(Set<Tourguide> ratedTourguides) {
        this.ratedTourguides = ratedTourguides;
    }

    public Set<Place> getCommentedPlaces() {
        return commentedPlaces;
    }

    public void setCommentedPlaces(Set<Place> commentedPlaces) {
        this.commentedPlaces = commentedPlaces;
    }

    public Set<Tourguide> getCommentedTourguides() {
        return commentedTourguides;
    }

    public void setCommentedTourguides(Set<Tourguide> commentedTourguides) {
        this.commentedTourguides = commentedTourguides;
    }
}
