package com.example.wannabemessenger.services;
import com.example.wannabemessenger.SQL.*;
import com.example.wannabemessenger.objectables.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;


@Service
public class commentService {
    private final commentsRepo commentsRepo;
    @Autowired
    public commentService(com.example.wannabemessenger.SQL.commentsRepo commentsRepo) {
        this.commentsRepo = commentsRepo;
    }
    public List<comment> getAllComments(){
        return commentsRepo.findAll();
    }
    public Optional<comment> getCommentByID(int id){
        return commentsRepo.findById(id);
    }
    public void deleteComment(comment comment){
        commentsRepo.delete(comment);
    }
    public List<comment> getCommentsByPost(post post){
        return commentsRepo.findCommentsByPost(post);
    }
    public void saveComment(comment comment){
        commentsRepo.save(comment);
    }
}
