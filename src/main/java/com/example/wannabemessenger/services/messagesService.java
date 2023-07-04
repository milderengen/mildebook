package com.example.wannabemessenger.services;

import com.example.wannabemessenger.objectables.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.example.wannabemessenger.SQL.*;

import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class messagesService {
    private final messagesRepo messagesRepo;
    @Autowired
    public messagesService(com.example.wannabemessenger.SQL.messagesRepo messagesRepo) {
        this.messagesRepo = messagesRepo;
    }
    public List<message> getAllMessages(){
        return messagesRepo.findAll();
    }
    public List<message> getMessagesByChatID(int id){
        List<message> messages = getAllMessages();
        List<message> sorted = messages.stream()
                .filter(message -> message.getChat().getId() == id).toList();
        return sorted;
    }
    public Optional<message> getMessageByMessageID(int ID){
        return messagesRepo.findById(ID);
    }
    public void saveMessage(message message){
        messagesRepo.save(message);
    }
    public void deleteMessage(message message){
        messagesRepo.delete(message);
    }

    public message getLastMessageOfAChat(int chatID){
        List<message> messages = messagesRepo.findByChatIdOrderByTimestampDesc(chatID);
        if (!messages.isEmpty()) {
            return messages.get(0);
        }else{
            return null;
        }
    }
}
