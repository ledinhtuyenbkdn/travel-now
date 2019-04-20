package com.ledinhtuyenbkdn.travelnow.model;

public class RateDTO {
    private Long id;
    private Integer numberStar;
    private Long touristId;

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

    public Long getTouristId() {
        return touristId;
    }

    public void setTouristId(Long touristId) {
        this.touristId = touristId;
    }
}
