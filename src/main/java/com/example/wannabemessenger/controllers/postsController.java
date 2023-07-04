package com.example.wannabemessenger.controllers;

import com.example.wannabemessenger.objectables.post;
import com.example.wannabemessenger.objectables.user;
import com.example.wannabemessenger.services.commentService;
import com.example.wannabemessenger.services.postsService;
import com.example.wannabemessenger.services.usersService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import com.example.wannabemessenger.generalFunctions;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.Optional;
@Controller
public class postsController {

    private generalFunctions generalFunctions;
    @Autowired
    private postsService postsService;
    @Autowired
    private commentService commentService;
    @Autowired
    private usersService usersService;
    @GetMapping("/post")
    public String post(Model model, HttpSession session, @RequestParam(name = "postID", required = true) int postID){
        Optional<post> optionalPost = postsService.findPostByID(postID);
        post post = optionalPost.get();

        model.addAttribute("post", post);
        model.addAttribute("user", generalFunctions.myUserFromSession(session, usersService));
        model.addAttribute("comments", commentService.getCommentsByPost(post));
        return "post";
    }
    @PostMapping("/postCreation")
    public String createPost(@RequestParam(name = "content") String content,
                             @RequestParam(name = "headline") String headline,HttpSession session){
        user user = generalFunctions.myUserFromSession(session, usersService);
        post post = new post();
        post.setAuthor(user);
        post.setTimestamp(generalFunctions.getTime());
        post.setHeadline(headline);
        post.setContent(content);
        postsService.savePost(post);
        return "redirect:/content";
    }
    @PostMapping("postUpdate")
    public String updatePost(@RequestParam(name = "postID") int postID,
                             @RequestParam(name = "parameter") String parameter,
                             @RequestParam(name = "changed") String changed, RedirectAttributes redirectAttributes){
        Optional<post> optionalPost = postsService.findPostByID(postID);
        post post = optionalPost.get();
        if(parameter.equals("headline")){
            post.setHeadline(changed);
        }else{
            post.setContent(changed);
        }
        postsService.savePost(post);
        redirectAttributes.addAttribute("postID", postID);
        return "redirect:post";
    }
    @PostMapping("postDelete")
    public String deletePost(@RequestParam(name = "postID") int postID, RedirectAttributes redirectAttributes){
        Optional<post> optionalPost = postsService.findPostByID(postID);
        post post = optionalPost.get();
        postsService.deletePost(post);
        return "contents";
    }
}
