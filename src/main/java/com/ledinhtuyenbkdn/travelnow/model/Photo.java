package com.ledinhtuyenbkdn.travelnow.model;

import org.neo4j.ogm.annotation.GeneratedValue;
import org.neo4j.ogm.annotation.Id;
import org.neo4j.ogm.annotation.NodeEntity;

@NodeEntity
public class Photo {
    @Id
    @GeneratedValue
    private Long id;
    private String urlImage;

    public Photo() {
    }

    public Photo(Long id, String urlImage) {
        this.id = id;
        this.urlImage = urlImage;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUrlImage() {
        return urlImage;
    }

    public void setUrlImage(String urlImage) {
        this.urlImage = urlImage;
    }
}
