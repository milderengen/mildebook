package com.example.wannabemessenger.controllers;
import com.example.wannabemessenger.generalFunctions;
import com.example.wannabemessenger.objectables.comment;
import com.example.wannabemessenger.objectables.post;
import com.example.wannabemessenger.objectables.user;
import com.example.wannabemessenger.services.commentService;
import com.example.wannabemessenger.services.postsService;
import com.example.wannabemessenger.services.usersService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.Optional;
@Controller
public class commentsController {
    @Autowired
    private postsService postsService;
    @Autowired
    private commentService commentService;
    @Autowired
    private usersService usersService;

    private generalFunctions generalFunctions;
    @PostMapping("/postComment")
    public String postComment(@RequestParam(name = "postID", required = true) int postID,
                              @RequestParam(name = "content") String content,
                              @RequestParam(name = "userID") int userID, HttpSession session){
        user user = generalFunctions.myUserFromSession(session, usersService);
        Optional<post> post = postsService.findPostByID(postID);
        comment comment = new comment();
        comment.setAuthor(user);
        comment.setPost(post.get());
        comment.setContent(content);
        comment.setPosted(generalFunctions.getTime());
        return "redirect:post";
    }
    @PostMapping("/deleteComment")
    public String deleteComment(@RequestParam(name = "commentID") int commentID, RedirectAttributes redirectAttributes){
        Optional<comment> comment = commentService.getCommentByID(commentID);
        post post = comment.get().getPost();
        commentService.deleteComment(comment.get());
        redirectAttributes.addAttribute("postID", post.getId());
        return "redirect:post";
    }
    @PostMapping("updateComment")
    public String updateComment(@RequestParam(name = "commentID") int commentID,
                                @RequestParam(name = "edited") String edited,RedirectAttributes redirectAttributes){
        Optional<comment> comment = commentService.getCommentByID(commentID);   //POPRIDAVAJ TAM TIE isPresent() lebo ked niekto zavola endpoint cez Postmana s jeblym ID tak vygrcia chybu
        post post = comment.get().getPost();
        commentService.saveComment(comment.get());
        redirectAttributes.addAttribute("postID", post.getId());
        return "redirect:post";
    }
}
