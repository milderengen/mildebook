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
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.Optional;
@Controller
public class messagesController {
    private generalFunctions generalFunctions;
    @Autowired
    private usersService usersService;
    @Autowired
    private chatsService chatsService;
    @Autowired
    private messagesService messagesService;
    @PostMapping("/messageSender")
    public String sendMessage(@RequestParam(name = "chatID") int chatID,
                              @RequestParam(name = "userID") int userID,
                              @RequestParam(name = "content") String content, RedirectAttributes redirectAttributes, HttpSession session){
        if(content.isEmpty()){
            redirectAttributes.addAttribute("chatID", chatID);
            return "redirect:chat";
        }
        if(userID==generalFunctions.myUserFromSession(session, usersService).getId()){
            Optional<chat> optionalChat = chatsService.getChatByID(chatID);
            chat chat = optionalChat.get();
            if(userID!=chat.getChatter1().getId() && userID!=chat.getChatter2().getId()){
                redirectAttributes.addAttribute("error", 10);   //unauthorized
                return "redirect:contents";
            }
            user user = generalFunctions.myUserFromSession(session, usersService);
            message message = new message();
            message.setAuthor(user);
            message.setChat(chat);
            message.setTimestamp(generalFunctions.getTime());
            message.setContent(content);
            messagesService.saveMessage(message);
            redirectAttributes.addAttribute("chatID", chatID);
            return "redirect:chat";
        }else{
            redirectAttributes.addAttribute("error", 10);   //unauthorized
            return "redirect:contents";
        }

    }
    @PostMapping("/messageSender/img")
    public String sendImg(@RequestParam(name = "chatID") int chatID,
                          @RequestParam(name = "img") MultipartFile multipartFile, RedirectAttributes redirectAttributes, HttpSession session){
        Optional<chat> optionalChat = chatsService.getChatByID(chatID);
        chat chat = optionalChat.get();
        user user = generalFunctions.myUserFromSession(session, usersService);
        if(generalFunctions.checkFile(multipartFile)==0){
            String safeFileName = generalFunctions.generateSafeFileName(multipartFile.getOriginalFilename());
            message message = new message();
            message.setAuthor(user);
            message.setChat(chat);
            message.setImage(safeFileName);
            message.setTimestamp(generalFunctions.getTime());
        }
        redirectAttributes.addAttribute("chatID", chatID);
        return "redirect:chat";
    }
    @PostMapping("/messageDeletion")
    public String deleteMessage(@RequestParam(name = "messageID") int messageID,RedirectAttributes redirectAttributes){
        Optional<message> optionalMessage = messagesService.getMessageByMessageID(messageID);
        if(optionalMessage.isPresent()){
            redirectAttributes.addAttribute("chatID", optionalMessage.get().getChat().getId());
            optionalMessage.get().setDeleted(true);
            optionalMessage.get().setDeletionDate(generalFunctions.getTime());
        }

        return "redirect:/chat";
    }
}
