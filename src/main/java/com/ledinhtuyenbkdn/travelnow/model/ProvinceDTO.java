package com.ledinhtuyenbkdn.travelnow.model;

public class ProvinceDTO {
    private Long id;
    private String nameProvince;
    private int numPlaces;

    public ProvinceDTO() {
    }

    public ProvinceDTO(Long id, String nameProvince, int numPlaces) {
        this.id = id;
        this.nameProvince = nameProvince;
        this.numPlaces = numPlaces;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNameProvince() {
        return nameProvince;
    }

    public void setNameProvince(String nameProvince) {
        this.nameProvince = nameProvince;
    }

    public int getNumPlaces() {
        return numPlaces;
    }

    public void setNumPlaces(int numPlaces) {
        this.numPlaces = numPlaces;
    }
}
