package com.ledinhtuyenbkdn.travelnow.model;

import org.neo4j.ogm.annotation.*;

@RelationshipEntity(type = "RATED_TOURGUIDE")
public class RatedTourguide {
    @Id
    @GeneratedValue
    private Long id;
    private Integer numberStar;
    @StartNode
    private Tourist tourist;
    @EndNode
    private Tourguide tourguide;

    public RatedTourguide() {
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

    public Tourguide getTourguide() {
        return tourguide;
    }

    public void setTourguide(Tourguide tourguide) {
        this.tourguide = tourguide;
    }
}
