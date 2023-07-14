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

import java.util.ArrayList;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

public class postsControllerTest {
    private generalFunctions generalFunctions;
    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private postsService postsService;
    @MockBean
    private commentService commentService;
    @MockBean
    private usersService usersService;
    @Test
    public void getPostShouldReturnPost() throws Exception {
        user mockUser = new user();
        mockUser.setId(1);

        post mockPost = new post();
        mockPost.setId(1);
        mockPost.setAuthor(mockUser);

        when(postsService.findPostByID(anyInt())).thenReturn(Optional.of(mockPost));
        when(generalFunctions.myUserFromSession(any(HttpSession.class), any(usersService.class))).thenReturn(mockUser);
        when(commentService.getCommentsByPost(any(post.class))).thenReturn(new ArrayList<>());

        mockMvc.perform(MockMvcRequestBuilders.get("/post")
                        .sessionAttr("user", mockUser)
                        .param("postID", "1"))
                .andExpect(status().isOk())
                .andExpect(view().name("post"));
    }
    @Test
    public void createPostShouldRedirectToContent() throws Exception {
        user mockUser = new user();
        mockUser.setId(1);

        when(generalFunctions.myUserFromSession(any(HttpSession.class), any(usersService.class))).thenReturn(mockUser);
        doNothing().when(postsService).savePost(any(post.class));

        mockMvc.perform(MockMvcRequestBuilders.post("/postCreation")
                        .sessionAttr("user", mockUser)
                        .param("content", "testContent")
                        .param("headline", "testHeadline"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/content"));
    }
    @Test
    public void deletePostShouldRedirectToContents() throws Exception {
        user mockUser = new user();
        mockUser.setId(1);

        post mockPost = new post();
        mockPost.setId(1);
        mockPost.setAuthor(mockUser);

        when(postsService.findPostByID(anyInt())).thenReturn(Optional.of(mockPost));
        when(generalFunctions.myUserFromSession(any(HttpSession.class), any(usersService.class))).thenReturn(mockUser);
        doNothing().when(postsService).deletePost(any(post.class));

        mockMvc.perform(MockMvcRequestBuilders.post("/postDelete")
                        .sessionAttr("user", mockUser)
                        .param("postID", "1"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("contents"));
    }
    @Test
    public void editPostShouldRedirectToPost() throws Exception {
        user mockUser = new user();
        mockUser.setId(1);

        post mockPost = new post();
        mockPost.setId(1);
        mockPost.setAuthor(mockUser);
        mockPost.setHeadline("oldHeadline");
        mockPost.setContent("oldContent");

        when(postsService.findPostByID(anyInt())).thenReturn(Optional.of(mockPost));
        when(generalFunctions.myUserFromSession(any(HttpSession.class), any(usersService.class))).thenReturn(mockUser);
        doNothing().when(postsService).savePost(any(post.class));

        mockMvc.perform(MockMvcRequestBuilders.post("/postEdit")
                        .sessionAttr("user", mockUser)
                        .param("headline", "newHeadline")
                        .param("content", "newContent")
                        .param("postID", "1"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrlPattern("post*"));
    }
    @Test
    public void editPageShouldReturnEdit() throws Exception {
        user mockUser = new user();
        mockUser.setId(1);

        post mockPost = new post();
        mockPost.setId(1);
        mockPost.setAuthor(mockUser);

        when(postsService.findPostByID(anyInt())).thenReturn(Optional.of(mockPost));
        when(generalFunctions.myUserFromSession(any(HttpSession.class), any(usersService.class))).thenReturn(mockUser);

        mockMvc.perform(MockMvcRequestBuilders.get("/postEdit")
                        .sessionAttr("user", mockUser)
                        .param("postID", "1"))
                .andExpect(status().isOk())
                .andExpect(view().name("edit"));
    }
}
