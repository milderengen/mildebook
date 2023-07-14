package com.example.wannabemessenger.controllers;

import com.example.wannabemessenger.generalFunctions;
import com.example.wannabemessenger.objectables.*;
import com.example.wannabemessenger.services.*;

import jakarta.servlet.http.HttpSession;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.web.multipart.MultipartFile;

import java.util.Optional;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrlPattern;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class messagesControllerTest {
    @Autowired
    private MockMvc mockMvc;
    private generalFunctions generalFunctions;
    @MockBean
    private usersService usersService;
    @MockBean
    private chatsService chatsService;
    @MockBean
    private messagesService messagesService;

    @Test
    public void sendMessageShouldRedirectToChat() throws Exception {
        user mockUser = new user();
        mockUser.setId(1);

        chat mockChat = new chat();
        mockChat.setId(1);
        mockChat.setChatter1(mockUser);
        mockChat.setChatter2(mockUser);

        when(generalFunctions.myUserFromSession(any(HttpSession.class), any(usersService.class))).thenReturn(mockUser);
        when(chatsService.getChatByID(anyInt())).thenReturn(Optional.of(mockChat));
        doNothing().when(messagesService).saveMessage(any(message.class));

        mockMvc.perform(MockMvcRequestBuilders.post("/messageSender")
                        .sessionAttr("user", mockUser)
                        .param("chatID", "1")
                        .param("userID", "1")
                        .param("content", "testMessage"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrlPattern("chat*"));
    }
    @Test
    public void sendImgShouldRedirectToChat() throws Exception {
        user mockUser = new user();
        mockUser.setId(1);

        chat mockChat = new chat();
        mockChat.setId(1);

        MockMultipartFile file = new MockMultipartFile("img", "hello.png", MediaType.IMAGE_PNG_VALUE, "Hello, World!".getBytes());

        when(generalFunctions.myUserFromSession(any(HttpSession.class), any(usersService.class))).thenReturn(mockUser);
        when(generalFunctions.checkFile(any(MultipartFile.class))).thenReturn(0);
        when(generalFunctions.generateSafeFileName(anyString())).thenReturn("safeFileName");
        when(chatsService.getChatByID(anyInt())).thenReturn(Optional.of(mockChat));

        mockMvc.perform(MockMvcRequestBuilders.multipart("/messageSender/img")
                        .file(file)
                        .param("chatID", "1"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrlPattern("chat*"));
    }
    @Test
    public void deleteMessageShouldRedirectToChat() throws Exception {
        message mockMessage = new message();
        mockMessage.setId(1);

        chat mockChat = new chat();
        mockChat.setId(1);
        mockMessage.setChat(mockChat);

        when(messagesService.getMessageByMessageID(anyInt())).thenReturn(Optional.of(mockMessage));

        mockMvc.perform(MockMvcRequestBuilders.post("/messageDeletion")
                        .param("messageID", "1"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrlPattern("chat*"));
    }
}
