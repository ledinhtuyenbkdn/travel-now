package com.ledinhtuyenbkdn.travelnow.model;

public class LikedPostDTO {
    private Long id;
    private Long touristId;

    public LikedPostDTO() {
    }

    public LikedPostDTO(Long id, Long touristId) {
        this.id = id;
        this.touristId = touristId;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getTouristId() {
        return touristId;
    }

    public void setTouristId(Long touristId) {
        this.touristId = touristId;
    }
}
