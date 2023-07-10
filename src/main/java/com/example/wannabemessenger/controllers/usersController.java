package com.example.wannabemessenger.controllers;
import com.example.wannabemessenger.generalFunctions;
import com.example.wannabemessenger.objectables.chat;
import com.example.wannabemessenger.objectables.friendship;
import com.example.wannabemessenger.objectables.post;
import com.example.wannabemessenger.objectables.user;
import com.example.wannabemessenger.services.chatsService;
import com.example.wannabemessenger.services.friendshipsService;
import com.example.wannabemessenger.services.postsService;
import com.example.wannabemessenger.services.usersService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
@Controller
public class usersController {
    private generalFunctions generalFunctions = new generalFunctions();
    @Autowired
    private PasswordEncoder passwordEncoder;
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
                           @RequestParam("avatar") MultipartFile img,
                           @RequestParam("bio") String bio,
                           RedirectAttributes redirectAttributes){
        List<user> users = usersService.findAllUsers();
        for(int i = 0;i<users.size();i++){
            if(Objects.equals(users.get(i).getName(), username)){
                String err = "e1";
                redirectAttributes.addAttribute("error", err);
                return "redirect:register";
            }
        }
        if(username==null){String err = "e5";redirectAttributes.addAttribute("error", err);return "redirect:register";}
        if(img==null){String err = "e7";redirectAttributes.addAttribute("error", err);return "redirect:register";}
        if(password==null){String err = "e8";redirectAttributes.addAttribute("error", err);return "redirect:register";}
        if(username.length()<2){String err = "e5";redirectAttributes.addAttribute("error", err);return "redirect:register";}
        if(password.length()<5){String err = "e6";redirectAttributes.addAttribute("error", err);return "redirect:register";}
        if(!password.equals(password2)){String err = "e2";redirectAttributes.addAttribute("error", err);return "redirect:register";}
        switch (generalFunctions.checkFile(img)) {
            case 1 -> {
                redirectAttributes.addAttribute("error", "fileE1");
                return "redirect:register";
            }
            case 2 -> {
                redirectAttributes.addAttribute("error", "fileE2");
                return "redirect:register";
            }
            case 3 -> {
                redirectAttributes.addAttribute("error", "fileE3");
                return "redirect:register";
            }
            case 4 -> {
                redirectAttributes.addAttribute("error", "fileE4");
                return "redirect:register";
            }
        }
        if(generalFunctions.checkFile(img)==0){
            user one = new user();
            one.setName(username);
            one.setPassword(passwordEncoder.encode(password));
            one.setPassword(password);
            one.setBio(bio);
            String safeFileName = generalFunctions.generateSafeFileName(img.getOriginalFilename());
            one.setImg(safeFileName);
            usersService.saveUserToDB(one);
        }

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
            if(passwordEncoder.matches(password, matchingUser.get().getPassword())){
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
                String err = "e3";
                redirectAttributes.addAttribute("error", err);
                return "redirect:index";
            }
        }else{
            String err = "e4";
            redirectAttributes.addAttribute("error", err);
            return "redirect:index";
        }
    }
    @GetMapping("/profile")
    public String getProfile(@RequestParam(name = "userID") int id, Model model, HttpSession session){
        Optional<user> optionalUser = usersService.findUserByID(id);
        user user  = optionalUser.get();
        model.addAttribute("user", user);
        model.addAttribute("me", generalFunctions.myUserFromSession(session, usersService));
        boolean areFriends = areFriends(generalFunctions.myUserFromSession(session, usersService),user,user.getAllFriendships());
        model.addAttribute("areFriends", areFriends);
        return "profile";
    }
    @PostMapping("/updatePic")
    public String updatePic(@RequestParam(name = "userID") int id,
                            @RequestParam(name = "picPath") String pic,
                            Model model, HttpSession session, RedirectAttributes redirectAttributes){
        user me = generalFunctions.myUserFromSession(session,usersService);
        if(!userCheck(me,id,redirectAttributes)){return "redirect:profile";}
        me.setImg(pic);
        usersService.saveUserToDB(me);
        addUserToAttr(me,redirectAttributes);
        return "redirect:profile";
    }
    @PostMapping("updateBio")
    public String updateBio(@RequestParam(name = "userID") int id,
                            @RequestParam(name = "newBio") String bio,
                            Model model, HttpSession session, RedirectAttributes redirectAttributes){
        user me = generalFunctions.myUserFromSession(session,usersService);
        if(!userCheck(me,id,redirectAttributes)){return  "redirect:profile";}
        me.setBio(bio);
        usersService.saveUserToDB(me);
        addUserToAttr(me,redirectAttributes);
        return "redirect:profile";
    }
    @PostMapping("updateName")
    public String updateName(@RequestParam(name = "userID") int id,
                            @RequestParam(name = "newName") String name,HttpSession session, RedirectAttributes redirectAttributes){
        user me = generalFunctions.myUserFromSession(session,usersService);
        if(!userCheck(me,id,redirectAttributes)){return  "redirect:profile";}
        me.setName(name);
        usersService.saveUserToDB(me);
        addUserToAttr(me,redirectAttributes);
        return "redirect:profile";
    }
    @PostMapping("changePrivacy")
    public String changePrivacy(@RequestParam(name = "userID") int id,
                                @RequestParam(name = "privacy") String privacy, RedirectAttributes redirectAttributes, HttpSession session){
        Optional<user> optionalUser = usersService.findUserByID(id);
        if(optionalUser.isPresent() && optionalUser.get()==generalFunctions.myUserFromSession(session,usersService)){
            if(privacy.equals("public") && optionalUser.get().isPrivateAcc()){optionalUser.get().setPrivateAcc(false);}
            if(privacy.equals("private") && !optionalUser.get().isPrivateAcc()){optionalUser.get().setPrivateAcc(true);}
            usersService.saveUserToDB(optionalUser.get());
        }
        redirectAttributes.addAttribute("userID", id);
        return "redirect:profile";
    }
    private boolean userCheck(user user, int id, RedirectAttributes redirectAttributes){
        if(user.getId()!=id){
            redirectAttributes.addFlashAttribute("userID",user.getId());
            redirectAttributes.addAttribute("error","unauthorized");    // we could create a function theoretically
            return  false;
        }
        return true;
    }
    private void addUserToAttr(user me, RedirectAttributes redirectAttributes){
        redirectAttributes.addAttribute("userID",me.getId());
    }
    public boolean areFriends(user me, user otherUser, List<friendship> combinedFriendships) {
        return combinedFriendships.stream().anyMatch(friendship ->
                friendship.getFriend().equals(otherUser) || friendship.getPerson().equals(otherUser)
        );
    }
}
