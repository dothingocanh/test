package com.example.tytb1.Model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class ReplyTwit {
    private long id;
    private String content;
    private String image;
    private String video;
    private User user;
    private String createdAt;
    private int totalLikes;
    private int totalReplies;
    private int totalRetweets;
    private List<ReplyTwit> replyTwits;
    private List<Integer> retwitUsersId;
    private boolean liked;
    private boolean retwit;
    private Long twitId;
    private List<Integer> retwitUserId;

    public long getId() {
        return id;
    }
    public ReplyTwit(String content, Long twitId) {
        this.content = content;
        this.twitId = twitId;
        this.user = null; // Đảm bảo rằng user có thể null nếu không cần thiết
    }
    public List<ReplyTwit> getReplyTwits() {
        return replyTwits;
    }

    public Long getTwitId() {
        return twitId;
    }

    public void setTwitId(Long twitId) {
        this.twitId = twitId;
    }

    public void setReplyTwits(List<ReplyTwit> replyTwits) {
        this.replyTwits = replyTwits;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getVideo() {
        return video;
    }

    public void setVideo(String video) {
        this.video = video;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public int getTotalLikes() {
        return totalLikes;
    }

    public void setTotalLikes(int totalLikes) {
        this.totalLikes = totalLikes;
    }

    public int getTotalReplies() {
        return totalReplies;
    }

    public void setTotalReplies(int totalReplies) {
        this.totalReplies = totalReplies;
    }

    public int getTotalRetweets() {
        return totalRetweets;
    }

    public void setTotalRetweets(int totalRetweets) {
        this.totalRetweets = totalRetweets;
    }

    public List<Integer> getRetwitUsersId() {
        return retwitUsersId;
    }

    public void setRetwitUsersId(List<Integer> retwitUsersId) {
        this.retwitUsersId = retwitUsersId;
    }

    public boolean isLiked() {
        return liked;
    }

    public void setLiked(boolean liked) {
        this.liked = liked;
    }

    public boolean isRetwit() {
        return retwit;
    }

    public void setRetwit(boolean retwit) {
        this.retwit = retwit;
    }

    public List<Integer> getRetwitUserId() {
        return retwitUserId;
    }

    public void setRetwitUserId(List<Integer> retwitUserId) {
        this.retwitUserId = retwitUserId;
    }
}
