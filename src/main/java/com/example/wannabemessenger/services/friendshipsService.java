package com.example.wannabemessenger.services;

import com.example.wannabemessenger.SQL.*;
import com.example.wannabemessenger.objectables.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
public class friendshipsService {
    private final friendshipsRepo friendshipsRepo;
    @Autowired
    public friendshipsService(com.example.wannabemessenger.SQL.friendshipsRepo friendshipsRepo) {
        this.friendshipsRepo = friendshipsRepo;
    }
    public List<friendship> getAllFriendships(){
        return friendshipsRepo.findAll();
    }
    public void saveFriendship(friendship friendship){
        friendshipsRepo.save(friendship);
    }
    public List<friendship> friendshipsByID(user me){
        List<friendship> one = friendshipsRepo.findByPerson(me);
        List<friendship> two = friendshipsRepo.findByFriend(me);
        return Stream.concat(one.stream(), two.stream()).toList();
    }
    public List<user> getJustFriends(user me){
        List<friendship> friendships = friendshipsByID(me);
        return friendships.stream()
                .map(friendship -> friendship.getPerson().equals(me) ? friendship.getFriend() : friendship.getPerson()).toList();
    }
    public void deleteFriendship(friendship friendship){
        friendshipsRepo.delete(friendship);         //protection
    }
}
