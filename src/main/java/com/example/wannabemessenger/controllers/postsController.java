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
        model.addAttribute("author", post.getAuthor());
        model.addAttribute("me",generalFunctions.myUserFromSession(session, usersService));
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
//    @PostMapping("postUpdate")
//    public String updatePost(@RequestParam(name = "postID") int postID,
//                             @RequestParam(name = "parameter") String parameter,
//                             @RequestParam(name = "changed") String changed, RedirectAttributes redirectAttributes){
//        Optional<post> optionalPost = postsService.findPostByID(postID);
//        post post = optionalPost.get();
//        if(parameter.equals("headline")){
//            post.setHeadline(changed);
//        }else{
//            post.setContent(changed);
//        }
//        postsService.savePost(post);
//        redirectAttributes.addAttribute("postID", postID);
//        return "redirect:post";
//    }
    @PostMapping("postDelete")
    public String deletePost(@RequestParam(name = "postID") int postID, RedirectAttributes redirectAttributes, HttpSession session){
        Optional<post> optionalPost = postsService.findPostByID(postID);
        if(optionalPost.isPresent() && optionalPost.get().getAuthor()!=generalFunctions.myUserFromSession(session,usersService)){
            redirectAttributes.addAttribute("error","unauthorized");
            return "redirect:post";
        }
        post post = optionalPost.get();
        postsService.deletePost(post);
        return "contents";
    }
    @PostMapping("/postEdit")
    public String editPost(@RequestParam(name = "headline") String headline,
                           @RequestParam(name = "content") String content,
                           @RequestParam(name = "postID") int id, HttpSession session,RedirectAttributes redirectAttributes){
        Optional<post> optionalPost = postsService.findPostByID(id);
        redirectAttributes.addAttribute("postID",id);
        if(optionalPost.isPresent() && optionalPost.get().getAuthor()!=generalFunctions.myUserFromSession(session,usersService)){
            redirectAttributes.addAttribute("error","unauthorized");
            return "redirect:post";
        }
        if(optionalPost.isPresent()){
            if(optionalPost.get().getHeadline().equals(headline) && optionalPost.get().getContent().equals(content)){
                return  "redirect:post";
            }
            optionalPost.get().setDateOfModification(generalFunctions.getTime());
            optionalPost.get().setActive(false);

            post updatedPost = new post();
            updatedPost.setAuthor(optionalPost.get().getAuthor());
            updatedPost.setTimestamp(generalFunctions.getTime());
            updatedPost.setHeadline(headline);
            updatedPost.setContent(content);
            updatedPost.setDislikes(optionalPost.get().getDislikes());
            updatedPost.setLikes(optionalPost.get().getLikes());
            updatedPost.setOriginalPostId(optionalPost.get().getId());

            postsService.savePost(optionalPost.get());
            postsService.savePost(updatedPost);
        }
        redirectAttributes.addAttribute("postID", id);
        return "redirect:post";
    }
    @GetMapping("postEdit")
    public String editPage(@RequestParam(name = "postID") int id, RedirectAttributes redirectAttributes, HttpSession session, Model model){
        Optional<post> optionalPost = postsService.findPostByID(id);
        redirectAttributes.addAttribute("postID",id);
        if(optionalPost.isPresent() && optionalPost.get().getAuthor()!=generalFunctions.myUserFromSession(session,usersService)){
            redirectAttributes.addAttribute("error","unauthorized");
            return "redirect:post";
        }else optionalPost.ifPresent(post -> model.addAttribute("post", post));
        return "edit";

    }
}
