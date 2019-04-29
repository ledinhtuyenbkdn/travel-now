package com.ledinhtuyenbkdn.travelnow.model;

public class CategoryDTO {
    private Long id;
    private String nameCategory;
    private int numPlaces;

    public CategoryDTO() {
    }

    public CategoryDTO(Long id, String nameCategory, int numPlaces) {
        this.id = id;
        this.nameCategory = nameCategory;
        this.numPlaces = numPlaces;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNameCategory() {
        return nameCategory;
    }

    public void setNameCategory(String nameCategory) {
        this.nameCategory = nameCategory;
    }

    public int getNumPlaces() {
        return numPlaces;
    }

    public void setNumPlaces(int numPlaces) {
        this.numPlaces = numPlaces;
    }
}
