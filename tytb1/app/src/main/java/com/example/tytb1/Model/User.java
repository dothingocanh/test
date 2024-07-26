package com.example.tytb1.Model;

import java.util.ArrayList;
import java.util.List;

public class User {
    private Long id;
    private String fullName;
    private String website;
    private String birthDate;
    private String email;
    private String password;
    private String mobile;
    private String image;
    private String backgroundImage;
    private String bio;
    private String location;
    private boolean followed;
    private boolean login_with_google;
    private List<Twit> twit = new ArrayList<>();
    private List<Like> likes = new ArrayList<>();

    private List<User> followers = new ArrayList<>();
    private List<User> following = new ArrayList<>();

//     Constructor for registration
    public User(String fullName, String email, String password, String birthDate) {
        this.fullName = fullName;
        this.email = email;
        this.password = password;
        this.birthDate = birthDate;
    }

    public User(Long id, String fullName, String email, String image, String location) {
        this.id = id;
        this.fullName = fullName;
        this.email = email;
        this.image = image;
        this.location = location;
    }
    public User(String fullName, String website, String birthDate, String bio, List<User> followers, List<User> following) {
        this.fullName = fullName;
        this.website = website;
        this.birthDate = birthDate;
        this.bio = bio;
        this.followers = followers;
        this.following = following;
    }

    public User(String email, String password) {
        this.email = email;
        this.password = password;
    }

    public User(Long id, String fullName, String website, String birthDate, String email, String password, String mobile, String image, String backgroundImage, String bio, String location, boolean followed, boolean login_with_google, List<Twit> twit, List<Like> likes, List<User> followers, List<User> following) {
        this.id = id;
        this.fullName = fullName;
        this.website = website;
        this.birthDate = birthDate;
        this.email = email;
        this.password = password;
        this.mobile = mobile;
        this.image = image;
        this.backgroundImage = backgroundImage;
        this.bio = bio;
        this.location = location;
        this.followed = followed;
        this.login_with_google = login_with_google;
        this.twit = twit;
        this.likes = likes;
        this.followers = followers;
        this.following = following;
    }

    public User() {

    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getWebsite() {
        return website;
    }

    public void setWebsite(String website) {
        this.website = website;
    }

    public String getBirthDate() {
        return birthDate;
    }

    public void setBirthDate(String birthDate) {
        this.birthDate = birthDate;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getBackgroundImage() {
        return backgroundImage;
    }

    public void setBackgroundImage(String backgroundImage) {
        this.backgroundImage = backgroundImage;
    }

    public String getBio() {
        return bio;
    }

    public void setBio(String bio) {
        this.bio = bio;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public boolean followed() {
        return followed;
    }

    public void followed(boolean reg_user) {
        this.followed = reg_user;
    }

    public boolean isLogin_with_google() {
        return login_with_google;
    }

    public void setLogin_with_google(boolean login_with_google) {
        this.login_with_google = login_with_google;
    }

    public List<Twit> getTwit() {
        return twit;
    }

    public void setTwit(List<Twit> twit) {
        this.twit = twit;
    }

    public List<Like> getLikes() {
        return likes;
    }

    public void setLikes(List<Like> likes) {
        this.likes = likes;
    }

    public List<User> getFollowers() {
        return followers;
    }

    public void setFollowers(List<User> followers) {
        this.followers = followers;
    }

    public List<User> getFollowing() {
        return following;
    }

    public void setFollowing(List<User> following) {
        this.following = following;
    }
}
