package com.example.wannabemessenger.controllers;

import com.example.wannabemessenger.services.*;
import com.example.wannabemessenger.objectables.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import com.example.wannabemessenger.generalFunctions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.sql.Time;
import java.sql.Timestamp;

import java.util.*;
import java.util.stream.Collectors;

@Controller
public class controller {

    private generalFunctions generalFunctions;
    @Autowired
    private chatsService chatsService;
    @Autowired
    private friendshipsService friendshipsService;
    @Autowired
    private messagesService messagesService;
    @Autowired
    private postsService postsService;
    @Autowired
    private usersService usersService;
    @Autowired
    private friendRequestsService friendRequestsService;
    @Autowired
    private commentService commentService;



    @GetMapping("/findAFriend")
    public String findAFriend(@RequestParam(name = "name", required = true) String name, RedirectAttributes redirectAttributes){
        redirectAttributes.addAttribute("searchedUsers",name);
        return "redirect:contents";
    }
    @GetMapping("/contents")
    public String content(HttpSession session, Model model, @RequestParam(name = "searchedUsers", required = false) String search){
        if(!(search == null) && !search.isEmpty()){
            List<user> searched = usersService.findUsersBySearch(search);
            if(searched.size()==0){model.addAttribute("searchedString", "there are no users with this name");}
            else{model.addAttribute("searched",searched);}
        }
        String username = (String) session.getAttribute("username");
        if(username==null){
            System.out.println("Condition not met. Endpoint access denied.");
            return null;
        }
        List<post> friendsPosts = postsService.checkIfFriendPost(friendshipsService.getJustFriends(generalFunctions.myUserFromSession(session, usersService)));
        List<chat> myChats = chatsService.getChatsByID(generalFunctions.myUserFromSession(session, usersService).getId());
        model.addAttribute("me", generalFunctions.myUserFromSession(session, usersService));
        model.addAttribute("friendPosts", friendsPosts);
        model.addAttribute("chats", myChats);
        return "contents";
    }
    /*@GetMapping("/**") netuším čo s týmto tbh
    public String badRequest(){
        return "badRequest";
    }*/
    @GetMapping("/logout")
    public String logout(HttpSession session){
        session.invalidate();
        return "redirect:/";
    }

}
