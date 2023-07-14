package com.example.wannabemessenger.controllers;

import com.example.wannabemessenger.generalFunctions;
import com.example.wannabemessenger.objectables.user;
import jakarta.servlet.http.HttpSession;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import com.example.wannabemessenger.services.*;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static javax.management.Query.eq;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

public class controllerTest {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private usersService usersService;

    @MockBean
    private postsService postsService;

    @MockBean
    private chatsService chatsService;

    @MockBean
    private friendshipsService friendshipsService;

    @MockBean
    private com.example.wannabemessenger.generalFunctions generalFunctions;

    @Test
    public void findAFriendShouldRedirectToContents() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/findAFriend")
                        .param("name", "testName"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("contents?searchedUsers=testName"));
    }

    @Test
    public void contentShouldReturnContentsView() throws Exception {
        user mockUser = new user();
        when(generalFunctions.myUserFromSession(any(HttpSession.class), usersService)).thenReturn(mockUser);

        mockMvc.perform(MockMvcRequestBuilders.get("/contents"))
                .andExpect(status().isOk())
                .andExpect(view().name("contents"));
    }

    @Test
    public void logoutShouldInvalidateSessionAndRedirect() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/logout"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/"));
    }
}
