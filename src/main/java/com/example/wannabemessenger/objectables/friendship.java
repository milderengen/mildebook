package com.example.wannabemessenger.objectables;
import com.example.wannabemessenger.services.*;
import jakarta.persistence.*;

import java.sql.Timestamp;

@Entity
@Table(name = "Friendships")
public class friendship {

    @Id
    @Column(name = "ID")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @ManyToOne
    @JoinColumn(name="personID")
    private user person;

    @ManyToOne
    @JoinColumn(name="friendID")
    private user friend;

    @Column(name = "creationtimestamp")
    private Timestamp creationTimeStamp;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public user getPerson() {
        return person;
    }

    public void setPerson(user person) {
        this.person = person;
    }

    public user getFriend() {
        return friend;
    }

    public void setFriend(user friend) {
        this.friend = friend;
    }

    public Timestamp getCreationTimeStamp() {
        return creationTimeStamp;
    }

    public void setCreationTimeStamp(Timestamp creationTimeStamp) {
        this.creationTimeStamp = creationTimeStamp;
    }
}
