package com.example.tytb1.Model;

public class Like {
    private Long id;
    private Long userId;
    private Long twitId;
    private String createdAt;

    public Like(Long id, Long userId, Long twitId, String createdAt) {
        this.id = id;
        this.userId = userId;
        this.twitId = twitId;
        this.createdAt = createdAt;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public Long getTwitId() {
        return twitId;
    }

    public void setTwitId(Long twitId) {
        this.twitId = twitId;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }
}
