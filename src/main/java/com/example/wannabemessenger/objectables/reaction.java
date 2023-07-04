package com.example.wannabemessenger.objectables;
import jakarta.persistence.*;

import java.sql.Timestamp;

@Entity
public class reaction {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "timestamp")
    private Timestamp timestamp;



    @Column(name="reactionType")
    private String reactionType;

    @ManyToOne
    @JoinColumn(name = "userID")
    private user user;

    @ManyToOne
    @JoinColumn(name = "postID")
    private post post;

    public Timestamp getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Timestamp timestamp) {
        this.timestamp = timestamp;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getReactionType() {
        return reactionType;
    }

    public void setReactionType(String reactionType) {
        this.reactionType = reactionType;
    }

    public com.example.wannabemessenger.objectables.user getUser() {
        return user;
    }

    public void setUser(com.example.wannabemessenger.objectables.user user) {
        this.user = user;
    }

    public com.example.wannabemessenger.objectables.post getPost() {
        return post;
    }

    public void setPost(com.example.wannabemessenger.objectables.post post) {
        this.post = post;
    }
}
