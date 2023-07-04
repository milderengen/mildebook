package com.example.wannabemessenger.controllers;
import com.example.wannabemessenger.generalFunctions;
import com.example.wannabemessenger.objectables.chat;
import com.example.wannabemessenger.objectables.post;
import com.example.wannabemessenger.objectables.user;
import com.example.wannabemessenger.services.chatsService;
import com.example.wannabemessenger.services.friendshipsService;
import com.example.wannabemessenger.services.postsService;
import com.example.wannabemessenger.services.usersService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
@Controller
public class usersController {
    private generalFunctions generalFunctions = new generalFunctions();
    @Autowired
    private usersService usersService;
    @Autowired
    private chatsService chatsService;
    @Autowired
    private postsService postsService;
    @Autowired
    private friendshipsService friendshipsService;
    @GetMapping("/registration")
    public String pageReg(){
        return "register";
    }

    @RequestMapping("/registration")
    public String register(Model model, @RequestParam("name") String username,
                           @RequestParam("pass") String password,
                           @RequestParam("pass2") String password2,
                           @RequestParam("avatar") String img,
                           RedirectAttributes redirectAttributes){
        List<user> users = usersService.findAllUsers();
        for(int i = 0;i<users.size();i++){
            if(Objects.equals(users.get(i).getName(), username)){
                String err = "e1";
                redirectAttributes.addAttribute("error1", err);
                return "redirect:register";
            }
        }
        if(!password.equals(password2)){String err = "e2";redirectAttributes.addAttribute("error2", err);return "redirect:register";}
        user one = new user();
        one.setName(username);
        one.setPassword(password);
        one.setImg(img);
        usersService.saveUserToDB(one);
        return "index";
    }
    @GetMapping("/")
    public String pageLogin(){
        return "index";
    }

    @RequestMapping("/")
    public String login(Model model, @RequestParam("name") String username,
                        @RequestParam("password") String password,
                        RedirectAttributes redirectAttributes, HttpSession session){
        List<user> users = usersService.findAllUsers();
        Optional<user> matchingUser = users.stream()
                .filter(user -> user.getName().equals(username))
                .findFirst();
        if(matchingUser.isPresent()){
            if(matchingUser.get().getPassword().equals(password)){
                session.setAttribute("userID", matchingUser.get().getId());
                user user = matchingUser.get();
                List<chat> chats = chatsService.getChatsByID(matchingUser.get().getId());
                List<post> friendPosts = postsService.checkIfFriendPost(generalFunctions.findFriends(user, friendshipsService));
                session.setAttribute("user", user);
                String imagePath = "/resources/images/"+matchingUser.get().getImg();
                if(chats.size()!=0){
                    model.addAttribute("chats", chats);
                }
                model.addAttribute("friendPost", friendPosts);
                model.addAttribute("chats", chats);
                model.addAttribute("user", user);
                model.addAttribute("img", imagePath);
                return "contents";
            }else{
                String err = "badLogin";
                redirectAttributes.addAttribute("error3", err);
                return "redirect:index";
            }
        }else{
            String err = "userNotFound";
            redirectAttributes.addAttribute("error4", err);
            return "redirect:index";
        }
    }
    @GetMapping("/profile")
    public String getProfile(@RequestParam(name = "userID") int id, Model model){
        Optional<user> optionalUser = usersService.findUserByID(id);
        user user  = optionalUser.get();
        model.addAttribute("user", user);

        return "profile";
    }
}
