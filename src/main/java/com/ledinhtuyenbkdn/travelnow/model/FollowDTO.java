package com.ledinhtuyenbkdn.travelnow.model;

public class FollowDTO {
    private Long id;
    private Long followerId;
    private Long followeeId;

    public FollowDTO() {
    }

    public FollowDTO(Long id, Long followerId, Long followeeId) {
        this.id = id;
        this.followerId = followerId;
        this.followeeId = followeeId;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getFollowerId() {
        return followerId;
    }

    public void setFollowerId(Long followerId) {
        this.followerId = followerId;
    }

    public Long getFolloweeId() {
        return followeeId;
    }

    public void setFolloweeId(Long followeeId) {
        this.followeeId = followeeId;
    }
}
