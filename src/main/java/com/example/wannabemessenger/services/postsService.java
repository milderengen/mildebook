package com.example.wannabemessenger.services;

import com.example.wannabemessenger.objectables.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.example.wannabemessenger.SQL.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class postsService {
    private final postsRepo postsRepo;
    @Autowired
    public postsService(com.example.wannabemessenger.SQL.postsRepo postsRepo) {
        this.postsRepo = postsRepo;
    }

    public List<post> findAllPosts(){
        return postsRepo.findAll();
    }
    public List<post>checkIfFriendPost(List<user> friends){
        List<post> posts = findAllPosts();
        return posts.stream()
                .filter(post -> post.isActive() && !post.isDeleted() && friends.stream().anyMatch(friend -> friend.getId()==(post.getAuthor().getId())))
                .collect(Collectors.toList());
    }
    public Optional<post> findPostByID(int postID){
        return postsRepo.findById(postID);
    }
    public void savePost(post post){
        postsRepo.save(post);
    }
    public void deletePost(post post){                  //mohol by si vytvoriť stránku na ktoru odkážeš že táto stranka už neexistuje a hodiť tam len link domov
        postsRepo.delete(post);
    }
}
