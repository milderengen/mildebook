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

import java.util.Optional;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrlPattern;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class reactionsControllerTest {
    private generalFunctions generalFunctions;
    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private postsService postsService;
    @MockBean
    private reactionsService reactionsService;

    @Test
    public void likePostShouldRedirectToPost() throws Exception {
        user mockUser = new user();
        mockUser.setId(1);

        post mockPost = new post();
        mockPost.setId(1);
        mockPost.setLikes(0);

        when(postsService.findPostByID(anyInt())).thenReturn(Optional.of(mockPost));
        when(generalFunctions.myUserFromSession(any(HttpSession.class), any(usersService.class))).thenReturn(mockUser);
        when(generalFunctions.fillReaction(anyString(), any(post.class), any(HttpSession.class), any(usersService.class))).thenReturn(new reaction());
        doNothing().when(reactionsService).saveReaction(any(reaction.class));

        mockMvc.perform(MockMvcRequestBuilders.post("/like")
                        .sessionAttr("user", mockUser)
                        .param("postID", "1"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrlPattern("post*"));
    }
    @Test
    public void dislikePostShouldRedirectToPost() throws Exception {
        user mockUser = new user();
        mockUser.setId(1);

        post mockPost = new post();
        mockPost.setId(1);
        mockPost.setDislikes(0);

        when(postsService.findPostByID(anyInt())).thenReturn(Optional.of(mockPost));
        when(generalFunctions.myUserFromSession(any(HttpSession.class), any(usersService.class))).thenReturn(mockUser);
        when(generalFunctions.fillReaction(anyString(), any(post.class), any(HttpSession.class), any(usersService.class))).thenReturn(new reaction());
        doNothing().when(reactionsService).saveReaction(any(reaction.class));

        mockMvc.perform(MockMvcRequestBuilders.post("/dislike")
                        .sessionAttr("user", mockUser)
                        .param("postID", "1"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrlPattern("post*"));
    }
}
