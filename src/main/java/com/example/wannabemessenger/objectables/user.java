package com.example.wannabemessenger.objectables;
import com.example.wannabemessenger.services.*;
import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.List;


@Entity
@Table(name = "Users")
public class user {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "name")
    private String name;
    @Column(name = "numoffriends")
    private Integer numOfFriends = 0;

    @Column(name = "img")
    private String img;

    @Column(name = "private")
    private boolean privateAcc = false;

    @Column(name = "bio")
    private String bio;
    @Column(name = "password")
    private String password;

    @OneToMany(mappedBy = "author")
    private List<post> posts;
    @OneToMany(mappedBy = "author")
    private List<message> messages;

    @OneToMany(mappedBy = "person")
    private List<friendship> friendships = new ArrayList<>();
    @OneToMany(mappedBy = "friend")
    private List<friendship> friendships2 = new ArrayList<>();

    public List<friendship> getAllFriendships(){
        List<friendship> combinedList = new ArrayList<>(friendships);
        combinedList.addAll(friendships2);
        return combinedList;
    }
    public String getBio() {
        return bio;
    }

    public void setBio(String bio) {
        this.bio = bio;
    }
    public Integer getNumOfFriends() {
        return numOfFriends;
    }

    public void setNumOfFriends(Integer numOfFriends) {
        this.numOfFriends = numOfFriends;
    }
    public String getImg() {
        return img;
    }

    public void setImg(String img) {
        this.img = img;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public boolean isPrivateAcc() {
        return privateAcc;
    }

    public void setPrivateAcc(boolean privateAcc) {
        this.privateAcc = privateAcc;
    }
    public List<post> getPosts() {
        return posts;
    }

    public void setPosts(List<post> posts) {
        this.posts = posts;
    }

    public List<message> getMessages() {
        return messages;
    }

    public void setMessages(List<message> messages) {
        this.messages = messages;
    }

    public List<friendship> getFriendships() {
        return friendships;
    }

    public void setFriendships(List<friendship> friendships) {
        this.friendships = friendships;
    }




}
