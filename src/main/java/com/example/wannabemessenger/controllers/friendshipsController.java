package com.example.wannabemessenger.controllers;
import com.example.wannabemessenger.generalFunctions;
import com.example.wannabemessenger.objectables.friendship;
import com.example.wannabemessenger.objectables.user;
import com.example.wannabemessenger.services.chatsService;
import com.example.wannabemessenger.services.usersService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import com.example.wannabemessenger.services.friendshipsService;

import java.util.List;
import java.util.Optional;
@Controller
public class friendshipsController {
    private generalFunctions generalFunctions;
    @Autowired
    private friendshipsService friendshipsService;
    @Autowired
    private usersService usersService;
    @Autowired
    private chatsService chatsService;

    //Friendship creation is in friendRequestController in function accepted
    @GetMapping("/friends")
    public String myFriends(HttpSession session, Model model){
        user me = generalFunctions.myUserFromSession(session, usersService);
        model.addAttribute("generalFunctions", generalFunctions);
        model.addAttribute("myChats", chatsService.getChatsByID(me.getId()));
        model.addAttribute("friends", friendshipsService.getJustFriends(me));
        model.addAttribute("me", me);
        return "friendlist";
    }
    @PostMapping("/endOfFriendship")
    public String unfriend(@RequestParam(name = "friendID") int friendID, HttpSession session){
        List<friendship> friendships = friendshipsService.getAllFriendships();
        Optional<user> optionalFriend = usersService.findUserByID(friendID);
        user friend = optionalFriend.get();
        List<friendship> filteredFriendships = friendships.stream()
                .filter(friendship ->
                        (friendship.getPerson().equals(generalFunctions.myUserFromSession(session, usersService)) && friendship.getFriend().equals(friend)) ||
                                (friendship.getPerson().equals(friend) && friendship.getFriend().equals(generalFunctions.myUserFromSession(session, usersService)))).toList();
        friendshipsService.deleteFriendship(filteredFriendships.get(0));
        filteredFriendships.get(0).getFriend().setNumOfFriends(filteredFriendships.get(0).getFriend().getNumOfFriends()-1);
        filteredFriendships.get(0).getPerson().setNumOfFriends(filteredFriendships.get(0).getPerson().getNumOfFriends()-1);
        return"redirect:/friendlist";
    }
}
