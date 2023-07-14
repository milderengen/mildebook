package com.example.wannabemessenger.controllers;

import com.example.wannabemessenger.generalFunctions;
import com.example.wannabemessenger.objectables.user;
import com.example.wannabemessenger.services.*;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.util.ArrayList;
import java.util.Arrays;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

public class usersControllerTest {
    private generalFunctions generalFunctions;
    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private PasswordEncoder passwordEncoder;
    @MockBean
    private usersService usersService;
    @MockBean
    private chatsService chatsService;
    @MockBean
    private postsService postsService;
    @MockBean
    private friendshipsService friendshipsService;
    @Test
    public void registerUserShouldReturnIndex() throws Exception {
        MockMultipartFile mockImg = new MockMultipartFile("imagefile", "testing.jpg", "image/jpg", "test image".getBytes());

        when(usersService.findAllUsers()).thenReturn(new ArrayList<>());
        when(generalFunctions.checkFile(any())).thenReturn(0);
        when(passwordEncoder.encode(anyString())).thenReturn("encodedPassword");
        doNothing().when(usersService).saveUserToDB(any(user.class));

        mockMvc.perform(MockMvcRequestBuilders.multipart("/registration")
                        .file(mockImg)
                        .param("name", "testUser")
                        .param("pass", "testPass")
                        .param("pass2", "testPass")
                        .param("bio", "testBio"))
                .andExpect(status().isOk())
                .andExpect(view().name("index"));
    }
    @Test
    public void loginUserShouldReturnContents() throws Exception {
        user mockUser = new user();
        mockUser.setId(1);
        mockUser.setName("testUser");
        mockUser.setPassword("testPass");

        when(usersService.findAllUsers()).thenReturn(Arrays.asList(mockUser));
        when(passwordEncoder.matches(anyString(), anyString())).thenReturn(true);
        when(chatsService.getChatsByID(anyInt())).thenReturn(new ArrayList<>());
        when(postsService.checkIfFriendPost(anyList())).thenReturn(new ArrayList<>());
        when(generalFunctions.findFriends(any(user.class), any(friendshipsService.class))).thenReturn(new ArrayList<>());

        mockMvc.perform(MockMvcRequestBuilders.post("/")
                        .param("name", "testUser")
                        .param("password", "testPass"))
                .andExpect(status().isOk())
                .andExpect(view().name("contents"));
    }
}
