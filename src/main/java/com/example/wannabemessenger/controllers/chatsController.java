package com.example.wannabemessenger.controllers;
import com.example.wannabemessenger.generalFunctions;
import com.example.wannabemessenger.objectables.chat;
import com.example.wannabemessenger.objectables.message;
import com.example.wannabemessenger.objectables.user;
import com.example.wannabemessenger.services.chatsService;
import com.example.wannabemessenger.services.messagesService;
import com.example.wannabemessenger.services.usersService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.servlet.view.RedirectView;

import java.util.List;
import java.util.Optional;
@Controller
public class chatsController {
    @Autowired
    private chatsService chatsService;
    @Autowired
    private messagesService messagesService;
    @Autowired
    private usersService usersService;
    private generalFunctions generalFunctions;
    @GetMapping("/chat")
    public String chat(Model model, HttpSession session, @RequestParam(name = "chatID", required = true) int chatID, RedirectAttributes redirectAttributes){
        user me = generalFunctions.myUserFromSession(session, usersService);
        Optional<chat> chat = chatsService.getChatByID(chatID);
        List<message> messages = messagesService.getMessagesByChatID(chatID);

        if(chat.get().getChatter1().getId()!=(int)session.getAttribute("userID") && chat.get().getChatter2().getId()!=(int)session.getAttribute("userID")){
            String err = "forbidden";
            redirectAttributes.addAttribute("error403", err);
            return "badRequest";
        }
        user second = null;
        if(chat.get().getChatter1().getId()!= me.getId()){
            second=chat.get().getChatter1();
        }else{second=chat.get().getChatter2();}
        model.addAttribute("messages", messages);
        model.addAttribute("me", me);
        model.addAttribute("friend", second);

        return "chat";
    }
    @PostMapping("/createChat")
    public RedirectView newChat(HttpSession session, @RequestParam(name = "friendID") int friendID, RedirectAttributes redirectAttributes){
        chat chat = new chat();
        chat.setChatter1(generalFunctions.myUserFromSession(session,usersService));
        chat.setChatter2(usersService.findUserByID(friendID).get());
        chatsService.saveToDB(chat);
        redirectAttributes.addAttribute("chatID", chat.getId());
        return new RedirectView("/chat");
    }
}
