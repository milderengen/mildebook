package com.example.wannabemessenger.objectables;
import jakarta.persistence.*;

import java.sql.Timestamp;

@Entity
public class friendRequest {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "senderID")
    private user sender;

    @ManyToOne
    @JoinColumn(name = "receiverID")
    private user receiver;
    @Column(name="Status")
    private String status;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public user getSender() {
        return sender;
    }

    public void setSender(user sender) {
        this.sender = sender;
    }

    public user getReceiver() {
        return receiver;
    }

    public void setReceiver(user receiver) {
        this.receiver = receiver;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Timestamp getRequestCreationDate() {
        return requestCreationDate;
    }

    public void setRequestCreationDate(Timestamp requestCreationDate) {
        this.requestCreationDate = requestCreationDate;
    }

    @Column(name="requestCreationDate")
    private Timestamp requestCreationDate;

    // Getters and setters
}
