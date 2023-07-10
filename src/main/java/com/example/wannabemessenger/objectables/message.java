package com.example.wannabemessenger.objectables;
import com.example.wannabemessenger.services.*;
import jakarta.persistence.*;

import java.sql.Timestamp;
import java.util.Date;

@Entity
@Table(name = "Messages")
public class message {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @ManyToOne
    @JoinColumn(name="Author_id")
    private user author;

    @Column(name = "content")
    private String content;
    @Column(name = "deleted")
    private boolean deleted = false;
    @Column(name = "deletion_date")
    private Timestamp deletionDate;
    @ManyToOne
    @JoinColumn(name="Chat_id")
    private chat chat;

    @Column(name = "timestamp")
    private Timestamp timestamp;

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

    public com.example.wannabemessenger.objectables.chat getChat() {
        return chat;
    }

    public void setChat(com.example.wannabemessenger.objectables.chat chat) {
        this.chat = chat;
    }

    public Timestamp getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Timestamp timestamp) {
        this.timestamp = timestamp;
    }
    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
    public boolean isDeleted() {
        return deleted;
    }

    public void setDeleted(boolean deleted) {
        this.deleted = deleted;
    }

    public Timestamp getDeletionDate() {
        return deletionDate;
    }

    public void setDeletionDate(Timestamp deletionDate) {
        this.deletionDate = deletionDate;
    }
}
