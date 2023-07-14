package com.example.wannabemessenger.controllers;

import com.example.wannabemessenger.generalFunctions;
import com.example.wannabemessenger.objectables.*;
import com.example.wannabemessenger.services.*;
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
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

public class friendRequestControllerTest {
    @MockBean
    private generalFunctions generalFunctions;
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private usersService usersService;

    @MockBean
    private friendRequestsService friendRequestsService;

    @Test
    public void friendRequestPostShouldReturnIndex() throws Exception {
        user mockUser = new user();
        mockUser.setId(1);
        friendRequest mockFriendRequest = new friendRequest();
        mockFriendRequest.setId(1);

        when(generalFunctions.myUserFromSession(any(HttpSession.class), any(usersService.class))).thenReturn(mockUser);
        when(usersService.findAllUsers()).thenReturn(Collections.singletonList(mockUser));
        doNothing().when(friendRequestsService).saveRequest(any(friendRequest.class));

        mockMvc.perform(MockMvcRequestBuilders.post("/friendReq/send")
                        .sessionAttr("user", mockUser)
                        .param("id", "1"))
                .andExpect(status().isOk())
                .andExpect(view().name("index"));
    }
    @Test
    public void friendshipCreationShouldReturnIndex() throws Exception {
        user mockUser = new user();
        mockUser.setId(1);
        friendRequest mockFriendRequest = new friendRequest();
        mockFriendRequest.setId(1);
        mockFriendRequest.setSender(mockUser);
        mockFriendRequest.setReceiver(mockUser);

        when(generalFunctions.myUserFromSession(any(HttpSession.class), any(usersService.class))).thenReturn(mockUser);
        when(friendRequestsService.findRequestById(anyInt())).thenReturn(Optional.of(mockFriendRequest));
        doNothing().when(friendRequestsService).saveRequest(any(friendRequest.class));

        mockMvc.perform(MockMvcRequestBuilders.get("/friendReq/accepted")
                        .sessionAttr("user", mockUser)
                        .param("id", "1"))
                .andExpect(status().isOk())
                .andExpect(view().name("index"));
    }
    @Test
    public void friendshipFailedShouldReturnIndex() throws Exception {
        friendRequest mockFriendRequest = new friendRequest();
        mockFriendRequest.setId(1);

        when(friendRequestsService.findRequestById(anyInt())).thenReturn(Optional.of(mockFriendRequest));
        doNothing().when(friendRequestsService).saveRequest(any(friendRequest.class));

        mockMvc.perform(MockMvcRequestBuilders.get("/friendReq/rejected")
                        .param("id", "1"))
                .andExpect(status().isOk())
                .andExpect(view().name("index"));
    }
    @Test
    public void friendshipDeletionShouldReturnIndex() throws Exception {
        doNothing().when(friendRequestsService).deleteRequest(anyInt());

        mockMvc.perform(MockMvcRequestBuilders.get("/friendReq/deletion")
                        .param("id", "1"))
                .andExpect(status().isOk())
                .andExpect(view().name("index"));
    }
    @Test
    public void friendRequestShouldReturnFriendRequestView() throws Exception {
        user mockUser = new user();
        mockUser.setId(1);

        when(generalFunctions.myUserFromSession(any(HttpSession.class), any(usersService.class))).thenReturn(mockUser);
        when(friendRequestsService.myFriendRequests(any(user.class))).thenReturn(Collections.emptyList());
        when(friendRequestsService.mySentFriendRequests(any(user.class))).thenReturn(Collections.emptyList());

        mockMvc.perform(MockMvcRequestBuilders.get("/friendReq")
                        .sessionAttr("user", mockUser))
                .andExpect(status().isOk())
                .andExpect(view().name("friendRequest"));
    }
}
