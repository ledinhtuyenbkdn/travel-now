package com.ledinhtuyenbkdn.travelnow.model;

import org.neo4j.ogm.annotation.GeneratedValue;
import org.neo4j.ogm.annotation.Id;
import org.neo4j.ogm.annotation.NodeEntity;
import org.neo4j.ogm.annotation.Relationship;

import javax.validation.constraints.NotBlank;
import java.util.HashSet;
import java.util.Set;

@NodeEntity
public class Place {
    @Id
    @GeneratedValue
    private Long id;

    @NotBlank
    private String namePlace;
    private String about;

    @NotBlank
    private String address;
    private Double latitude;
    private Double longitude;
    @Relationship(type = "HAS_IMAGE", direction = Relationship.OUTGOING)
    private Set<Image> images = new HashSet<>();
    @Relationship(type = "IS_IN", direction = Relationship.OUTGOING)
    private Province province;
    @Relationship(type = "IS_BELONGS_TO", direction = Relationship.OUTGOING)
    private Category category;

    public Place() {
    }

    public Place(Long id, String namePlace, String about, String address, Double latitude, Double longitude, Set<Image> images, Province province, Category category) {
        this.id = id;
        this.namePlace = namePlace;
        this.about = about;
        this.address = address;
        this.latitude = latitude;
        this.longitude = longitude;
        this.images = images;
        this.province = province;
        this.category = category;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNamePlace() {
        return namePlace;
    }

    public void setNamePlace(String namePlace) {
        this.namePlace = namePlace;
    }

    public String getAbout() {
        return about;
    }

    public void setAbout(String about) {
        this.about = about;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public Double getLatitude() {
        return latitude;
    }

    public void setLatitude(Double latitude) {
        this.latitude = latitude;
    }

    public Double getLongitude() {
        return longitude;
    }

    public void setLongitude(Double longitude) {
        this.longitude = longitude;
    }

    public Set<Image> getImages() {
        return images;
    }

    public void setImages(Set<Image> images) {
        this.images = images;
    }

    public Province getProvince() {
        return province;
    }

    public void setProvince(Province province) {
        this.province = province;
    }

    public Category getCategory() {
        return category;
    }

    public void setCategory(Category category) {
        this.category = category;
    }
}
