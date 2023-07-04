package com.example.wannabemessenger.services;
import com.example.wannabemessenger.SQL.*;
import com.example.wannabemessenger.objectables.*;
import org.springframework.stereotype.Service;

import java.util.List;
@Service

public class reactionsService {
    private final reactionsRepo reactionsRepo;

    public reactionsService(com.example.wannabemessenger.SQL.reactionsRepo reactionsRepo) {
        this.reactionsRepo = reactionsRepo;
    }
    public List<reaction> getAllReactions(){
        return reactionsRepo.findAll();
    }
    public void saveReaction(reaction reaction){
        reactionsRepo.save(reaction);
    }
    public void deleteReaction(reaction reaction){
        reactionsRepo.delete(reaction);
    }
}
