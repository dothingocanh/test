package com.example.tytb1.Model;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class Twit {
    private Long id;
    private String content;
    private String image;
    private String video;


    private String createdAt;

    private User user;
    private int totalLikes;
    private int totalReplies;
    private int totalRetweets;
    private List<Integer> retwitUsersId;
    private boolean retwit;
    private boolean liked;
    private Long twitId;
    private List<ReplyTwit> replyTwits;

    public List<ReplyTwit> getReplyTwits() {
        return replyTwits;
    }

    public void setReplyTwits(List<ReplyTwit> replyTwits) {
        this.replyTwits = replyTwits;
    }

    //reply
    public Twit(String content, Long twitId) {
        this.content = content;
        this.twitId = twitId;
    }



    //create twit
    public Twit(String content, String image) {
        this.content = content;
        this.image = image;
    }
//////
    public Twit(Long id, String content, String image, String video, String createdAt, User user, int totalLikes, int totalReplies, int totalRetweets) {
        this.id = id;
        this.content = content;
        this.image = image;
        this.video = video;
        this.createdAt = createdAt;
        this.user = user;
        this.totalLikes = totalLikes;
        this.totalReplies = totalReplies;
        this.totalRetweets = totalRetweets;
    }

    public Long getTwitId() {
        return twitId;
    }

    public void setTwitId(Long twitId) {
        this.twitId = twitId;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
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

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
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

    public boolean isRetwit() {
        return retwit;
    }

    public void setRetwit(boolean retwit) {
        this.retwit = retwit;
    }

    public boolean isLiked() {
        return liked;
    }

    public void setLiked(boolean liked) {
        this.liked = liked;
    }
}
