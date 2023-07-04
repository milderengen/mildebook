package com.example.wannabemessenger.objectables;
import jakarta.persistence.*;
import jakarta.persistence.Entity;


import java.sql.Timestamp;

@Entity
public class comment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "postID")
    private post post;

    @ManyToOne
    @JoinColumn(name = "authorID")
    private user author;
    @Column(name="timestamp")
    private Timestamp posted;
    @Column(name="content")
    private String content;

    @Column(name="edited")
    private boolean edited = false;

    public boolean isEdited() {
        return edited;
    }

    public void setEdited(boolean edited) {
        this.edited = edited;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public com.example.wannabemessenger.objectables.post getPost() {
        return post;
    }

    public void setPost(com.example.wannabemessenger.objectables.post post) {
        this.post = post;
    }

    public user getAuthor() {
        return author;
    }

    public void setAuthor(user author) {
        this.author = author;
    }

    public Timestamp getPosted() {
        return posted;
    }

    public void setPosted(Timestamp posted) {
        this.posted = posted;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    // Getters and setters
}
