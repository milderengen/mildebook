package com.example.wannabemessenger.controllers;

import com.example.wannabemessenger.generalFunctions;
import com.example.wannabemessenger.objectables.*;
import com.example.wannabemessenger.services.*;
import com.example.wannabemessenger.services.friendRequestsService;
import com.example.wannabemessenger.services.usersService;
import jakarta.servlet.http.HttpSession;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.util.Collections;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

public class friendshipsControllerTest {
    @MockBean
    private generalFunctions generalFunctions;
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private usersService usersService;

    @MockBean
    private chatsService chatsService;
    @MockBean
    private friendshipsService friendshipsService;
    @Test
    public void myFriendsShouldReturnFriendListView() throws Exception {
        user mockUser = new user();
        mockUser.setId(1);

        when(generalFunctions.myUserFromSession(any(HttpSession.class), any(usersService.class))).thenReturn(mockUser);
        when(chatsService.getChatsByID(anyInt())).thenReturn(Collections.emptyList());
        when(friendshipsService.getJustFriends(any(user.class))).thenReturn(Collections.emptyList());

        mockMvc.perform(MockMvcRequestBuilders.get("/friends")
                        .sessionAttr("user", mockUser))
                .andExpect(status().isOk())
                .andExpect(view().name("friendlist"));
    }
    @Test
    public void unfriendShouldRedirectToFriendListView() throws Exception {
        user mockUser = new user();
        mockUser.setId(1);

        friendship mockFriendship = new friendship();
        mockFriendship.setFriend(mockUser);
        mockFriendship.setPerson(mockUser);

        when(generalFunctions.myUserFromSession(any(HttpSession.class), any(usersService.class))).thenReturn(mockUser);
        when(usersService.findUserByID(anyInt())).thenReturn(Optional.of(mockUser));
        when(friendshipsService.getAllFriendships()).thenReturn(Collections.singletonList(mockFriendship));
        doNothing().when(friendshipsService).deleteFriendship(any(friendship.class));

        mockMvc.perform(MockMvcRequestBuilders.post("/endOfFriendship")
                        .sessionAttr("user", mockUser)
                        .param("friendID", "1"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/friendlist"));
    }
}
