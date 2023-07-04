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
    @JoinColumn(name="AuthorID")
    private user author;
    private String content;

    @ManyToOne
    @JoinColumn(name="ChatID")
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
}
