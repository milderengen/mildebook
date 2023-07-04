package com.example.wannabemessenger.services;

import com.example.wannabemessenger.objectables.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.example.wannabemessenger.SQL.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class usersService {
    private final usersRepo usersRepo;
    /*@Autowired
    friendshipsService friendshipsService;*/
    @Autowired
    public usersService(com.example.wannabemessenger.SQL.usersRepo usersRepo) {
        this.usersRepo = usersRepo;
    }

    public List<user> findAllUsers(){
        return usersRepo.findAll();
    }
    public Optional<user> findUserByID(int ID){
        return usersRepo.findById(ID);
    }

    public void saveUserToDB(user user){
        usersRepo.save(user);
    }
    public List<user> findUsersBySearch(String search){
        return usersRepo.findByNameStartingWith(search);
    }
}
