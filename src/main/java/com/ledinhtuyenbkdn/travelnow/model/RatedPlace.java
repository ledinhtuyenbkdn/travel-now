package com.ledinhtuyenbkdn.travelnow.model;

import org.neo4j.ogm.annotation.*;

@RelationshipEntity(type = "RATED_PLACE")
public class RatedPlace {
    @Id
    @GeneratedValue
    private Long id;
    private Integer numberStar;
    @StartNode
    private Tourist tourist;
    @EndNode
    private Place place;

    public RatedPlace() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getNumberStar() {
        return numberStar;
    }

    public void setNumberStar(Integer numberStar) {
        this.numberStar = numberStar;
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
