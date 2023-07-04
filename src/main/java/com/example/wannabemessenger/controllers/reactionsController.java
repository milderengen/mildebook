package com.example.wannabemessenger.controllers;
import com.example.wannabemessenger.generalFunctions;
import com.example.wannabemessenger.objectables.post;
import com.example.wannabemessenger.objectables.reaction;
import com.example.wannabemessenger.services.*;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;
import java.util.Optional;
@Controller
public class reactionsController {
    private generalFunctions generalFunctions;
    @Autowired
    private chatsService chatsService;
    @Autowired
    private messagesService messagesService;
    @Autowired
    private postsService postsService;
    @Autowired
    private reactionsService reactionsService;
    @Autowired
    private usersService usersService;


    @PostMapping("/like")
    public String like(@RequestParam(name = "postID", required = true) int postID, RedirectAttributes redirectAttributes, HttpSession session){
        Optional<post> optionalPost = postsService.findPostByID(postID);
        post post = optionalPost.get();
        int likes = post.getLikes()+1;
        post.setLikes(likes);
        reaction reaction = generalFunctions.fillReaction("positive",post,session, usersService);
        reactionsService.saveReaction(reaction);
        redirectAttributes.addAttribute("postID", postID);
        return "redirect:post";
    }
    @PostMapping("/dislike")
    public String dislike(Model model, @RequestParam(name = "postID", required = true) int postID, RedirectAttributes redirectAttributes, HttpSession session){
        Optional<post> optionalPost = postsService.findPostByID(postID);
        post post = optionalPost.get();
        int dislikes = post.getDislikes()+1;
        post.setDislikes(dislikes);
        reaction reaction = generalFunctions.fillReaction("negative",post,session, usersService);
        reactionsService.saveReaction(reaction);
        redirectAttributes.addAttribute("postID", postID);
        return "redirect:post";
    }
}
