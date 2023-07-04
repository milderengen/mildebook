package com.example.wannabemessenger.SQL;

import com.example.wannabemessenger.objectables.friendRequest;
import com.example.wannabemessenger.objectables.user;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface friendRequestsRepo extends JpaRepository<friendRequest,Integer>{
    @Query("SELECT f FROM friendRequest f WHERE f.receiver = ?1")
    List<friendRequest> findFriendsByReceiver(user user);
    @Query("SELECT f FROM friendRequest f WHERE f.sender = ?1")
    List<friendRequest> findFriendsBySender(user user);
}
