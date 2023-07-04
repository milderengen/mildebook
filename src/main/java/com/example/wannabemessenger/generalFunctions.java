package com.example.wannabemessenger;

import com.example.wannabemessenger.objectables.*;
import com.example.wannabemessenger.services.*;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
@Service
public class generalFunctions {

    public List<user> findFriends(user user, friendshipsService friendshipsService){
        List<friendship> friendships = friendshipsService.getAllFriendships();
        System.out.println(friendships);
        if(friendships.isEmpty()){
            return Collections.emptyList();
        }
        return friendships.stream()
                .filter(friendship -> friendship.getFriend().equals(user) || friendship.getPerson().equals(user))
                .map(friendship -> {
                    if (friendship.getPerson().equals(user)) {
                        return friendship.getFriend();
                    } else {
                        return friendship.getPerson();
                    }
                })
                .collect(Collectors.toList());
    }
    public user myUserFromSession(HttpSession session, usersService usersService){
        int userID = (Integer) session.getAttribute("userID");
        Optional<user> user = usersService.findUserByID(userID);
        return user.get();
    }
    public Timestamp getTime(){
        long currentTimeMillis = System.currentTimeMillis();
        return new Timestamp(currentTimeMillis);
    }
    public reaction fillReaction(String emotion, post post, HttpSession session, usersService usersService){
        reaction reaction = new reaction();
        reaction.setPost(post);
        reaction.setUser(myUserFromSession(session, usersService));
        reaction.setTimestamp(getTime());
        if(emotion.equals("positive")){
            reaction.setReactionType("like");
        }else{reaction.setReactionType("dislike");}
        return reaction;
    }
    public Optional<chat> isThereAChat(user friend, List<chat> chats){
        return chats.stream()
                .filter(chat -> chat.getChatter1().equals(friend) || chat.getChatter2().equals(friend))
                .findAny();
    }
}
