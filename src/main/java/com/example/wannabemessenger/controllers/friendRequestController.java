package com.example.wannabemessenger.controllers;
import com.example.wannabemessenger.generalFunctions;
import com.example.wannabemessenger.objectables.friendRequest;
import com.example.wannabemessenger.objectables.friendship;
import com.example.wannabemessenger.objectables.user;
import com.example.wannabemessenger.services.friendRequestsService;
import com.example.wannabemessenger.services.friendshipsService;
import com.example.wannabemessenger.services.usersService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.sql.Timestamp;
import java.util.List;
import java.util.Optional;
@Controller
public class friendRequestController {
    @Autowired
    private usersService usersService;
    @Autowired
    private friendRequestsService friendRequestsService;
    @Autowired
    private friendshipsService friendshipsService;
    private generalFunctions generalFunctions;
    @PostMapping("friendReq/send")
    public String friendRequestPost(@RequestParam(name = "id") int userID, RedirectAttributes redirectAttributes, HttpSession session){
        friendRequest friendRequest = new friendRequest();
        friendRequest.setSender((user) session.getAttribute("user"));
        List<user> list = usersService.findAllUsers();
        for(int i = 0;i<list.size();i++){
            if(list.get(i).getId()==userID){
                friendRequest.setReceiver(list.get(i));
            }
        }
        long currentTimeMillis = System.currentTimeMillis();
        Timestamp timestamp = new Timestamp(currentTimeMillis);
        friendRequest.setRequestCreationDate(timestamp);
        friendRequest.setStatus("Sent");
        friendRequestsService.saveRequest(friendRequest);
        return "index";
    }
    @GetMapping("friendReq/accepted")
    public String friendshipCreation(@RequestParam(name = "id") int requestID, HttpSession session){
        friendship friendship = new friendship();
        Optional<friendRequest> optional = friendRequestsService.findRequestById(requestID);
        friendRequest friendRequest = optional.get();
        friendship.setPerson(friendRequest.getSender());
        friendship.setFriend((user)session.getAttribute("user"));
        friendship.setCreationTimeStamp(generalFunctions.getTime());
        friendshipsService.saveFriendship(friendship);
        friendRequest.setStatus("Accepted");
        friendRequestsService.saveRequest(friendRequest);
        friendRequest.getSender().setNumOfFriends(friendRequest.getSender().getNumOfFriends()+1);
        friendRequest.getReceiver().setNumOfFriends(friendRequest.getReceiver().getNumOfFriends()+1);
        return "index"; //napraviť
    }
    @GetMapping("friendReq/rejected")
    public String friendshipFailed(@RequestParam(name = "id") int requestID, HttpSession session){
        Optional<friendRequest>  optional = friendRequestsService.findRequestById(requestID);
        friendRequest friendRequest = optional.get();
        friendRequest.setStatus("Rejected");
        friendRequestsService.saveRequest(friendRequest);
        return "index"; //napraviť do piče
    }
    @GetMapping("friendReq/deletion")
    public String friendshipFailed(@RequestParam(name = "id") int requestID){
        friendRequestsService.deleteRequest(requestID);
        return "index"; //napraviť do piče
    }

    @GetMapping("friendReq")
    public String friendRequest(Model model, HttpSession session){
        user myUser = generalFunctions.myUserFromSession(session, usersService);
        List<friendRequest> toMe = friendRequestsService.myFriendRequests(myUser);
        List<friendRequest> fromMe = friendRequestsService.mySentFriendRequests(myUser);

        model.addAttribute("toMe", toMe);
        model.addAttribute("fromMe", fromMe);

        return "friendRequest";
    }
}
