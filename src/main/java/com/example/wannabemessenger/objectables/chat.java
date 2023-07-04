package com.example.wannabemessenger.objectables;
import com.example.wannabemessenger.services.*;
import jakarta.persistence.*;

import java.util.Comparator;
import java.util.List;
import java.util.Optional;


@Entity
@Table(name = "Chats")
public class chat {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @ManyToOne
    @JoinColumn(name="chatter1ID")
    private user chatter1;

    @ManyToOne
    @JoinColumn(name="chatter2ID")
    private user chatter2;

    @OneToMany(mappedBy = "chat")
    private List<message> messages;

    public static message getLatestMessage(List<message> messages1) {
        Optional<message> latestMessage = messages1.stream()
                .max(Comparator.comparing(message::getTimestamp));

        return latestMessage.orElse(null);
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public user getChatter1() {
        return chatter1;
    }

    public void setChatter1(user chatter1) {
        this.chatter1 = chatter1;
    }

    public user getChatter2() {
        return chatter2;
    }

    public void setChatter2(user chatter2) {
        this.chatter2 = chatter2;
    }

    public List<message> getMessages() {
        return messages;
    }

    public void setMessages(List<message> messages) {
        this.messages = messages;
    }
}
