package com.example.wannabemessenger.services;
import com.example.wannabemessenger.SQL.*;
import com.example.wannabemessenger.objectables.*;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class friendRequestsService {
    private final friendRequestsRepo friendRequestsRepo;

    public friendRequestsService(com.example.wannabemessenger.SQL.friendRequestsRepo friendRequestsRepo) {
        this.friendRequestsRepo = friendRequestsRepo;
    }
    public List<friendRequest> findAllFriendRequests(){
        return friendRequestsRepo.findAll();
    }
    public void saveRequest(friendRequest friendRequest){
        friendRequestsRepo.save(friendRequest);
    }
    public Optional<friendRequest> findRequestById(int id){
        return friendRequestsRepo.findById(id);

    }
    public List<friendRequest> myFriendRequests(user me){
        return friendRequestsRepo.findFriendsByReceiver(me);
    }
    public List<friendRequest> mySentFriendRequests(user me){
        return friendRequestsRepo.findFriendsBySender(me);
    }
    public void deleteRequest(int id){
        friendRequestsRepo.deleteById(id);
    }
}
