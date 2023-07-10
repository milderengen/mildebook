package com.example.wannabemessenger.services;

import com.example.wannabemessenger.SQL.*;
import com.example.wannabemessenger.objectables.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class chatsService {
    private final chatsRepo chatsRepo;

    @Autowired
    public chatsService(com.example.wannabemessenger.SQL.chatsRepo chatsRepo) {
        this.chatsRepo = chatsRepo;
    }
    public List<chat> getAllChats(){
        return chatsRepo.findAll();
    }
    public List<chat> getChatsByID(int ID){
        List<chat> chats = chatsRepo.findAll();
        return chats.stream()
                .filter(chat -> chat.getChatter1().getId() == ID || chat.getChatter2().getId() == ID).toList();
    }
    public Optional<chat> getChatByID(int ID){
        return chatsRepo.findById(ID);
    }
    public void saveToDB(chat chat){
        chatsRepo.save(chat);
    }



}
