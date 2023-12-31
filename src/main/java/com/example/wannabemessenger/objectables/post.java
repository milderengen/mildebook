package com.example.wannabemessenger.objectables;
import com.example.wannabemessenger.services.*;
import jakarta.persistence.*;

import java.sql.Timestamp;
import java.util.Date;
import java.util.List;


@Entity
@Table(name = "Posts")
public class post {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @ManyToOne
    @JoinColumn(name="author_id")
    private user author;

    @OneToMany(mappedBy = "post")
    private List<comment> comments;

    @OneToMany(mappedBy = "post")
    private List<reaction> reactions;

    @Column(name = "likes")
    private int likes = 0;

    @Column(name = "dislikes")
    private int dislikes = 0;

    @Column(name = "timestamp")
    private Timestamp timestamp;
    @Column(name = "headline")
    private String headline;
    @Column(name = "content")
    private String content;
    @Column(name = "deleted")
    private boolean deleted;
    @Column(name = "deleted_on")
    private Timestamp deletedOn;
    @Column(name = "active")
    private boolean active = true;
    @Column(name = "date_of_modification")
    private Timestamp dateOfModification;
    @Column(name = "original_post_id")
    private int originalPostId;

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public Timestamp getDateOfModification() {
        return dateOfModification;
    }

    public void setDateOfModification(Timestamp dateOfModification) {
        this.dateOfModification = dateOfModification;
    }

    public int getOriginalPostId() {
        return originalPostId;
    }

    public void setOriginalPostId(int originalPostId) {
        this.originalPostId = originalPostId;
    }

    public String getHeadline() {
        return headline;
    }

    public void setHeadline(String headline) {
        this.headline = headline;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
    public List<comment> getComments() {
        return comments;
    }

    public void setComments(List<comment> comments) {
        this.comments = comments;
    }

    public List<reaction> getReactions() {
        return reactions;
    }

    public void setReactions(List<reaction> reactions) {
        this.reactions = reactions;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public user getAuthor() {
        return author;
    }

    public void setAuthor(user author) {
        this.author = author;
    }

    public int getLikes() {
        return likes;
    }

    public void setLikes(int likes) {
        this.likes = likes;
    }

    public int getDislikes() {
        return dislikes;
    }

    public void setDislikes(int dislikes) {
        this.dislikes = dislikes;
    }

    public Timestamp getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Timestamp timestamp) {
        this.timestamp = timestamp;
    }
    public boolean isDeleted() {
        return deleted;
    }

    public void setDeleted(boolean deleted) {
        this.deleted = deleted;
    }

    public Timestamp getDeletedOn() {
        return deletedOn;
    }

    public void setDeletedOn(Timestamp deletedOn) {
        this.deletedOn = deletedOn;
    }
}




