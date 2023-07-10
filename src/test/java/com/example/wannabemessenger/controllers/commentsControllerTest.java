package com.example.wannabemessenger.controllers;
import com.example.wannabemessenger.generalFunctions;
import com.example.wannabemessenger.objectables.*;
import com.example.wannabemessenger.services.*;
import jakarta.servlet.http.HttpSession;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.util.Optional;

import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

public class commentsControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private com.example.wannabemessenger.services.postsService postsService;

    @MockBean
    private commentService commentService;

    @MockBean
    private usersService usersService;

    @Autowired
    private generalFunctions generalFunctions;

    @Test
    public void postCommentShouldRedirectToPostView() throws Exception {
        int postID = generalFunctions.randNum();
        int userID = generalFunctions.randNum();
        MockHttpSession session = new MockHttpSession();
        session.setAttribute("userID", userID);

        user mockUser = generalFunctions.myUserFromSession(session, usersService);
        post mockPost = generalFunctions.activeDummyPost();
        comment mockComment = generalFunctions.dummyComment();
        mockComment.setAuthor(mockUser);
        mockComment.setPost(mockPost);

        when(postsService.findPostByID(eq(postID))).thenReturn(Optional.of(mockPost));

        mockMvc.perform(MockMvcRequestBuilders.post("/postComment")
                        .session(session)
                        .param("postID", Integer.toString(postID))
                        .param("content", "Test Content"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("post"));
    }

    @Test
    public void deleteCommentShouldRedirectToPostView() throws Exception {
        int commentID = generalFunctions.randNum();
        comment mockComment = generalFunctions.dummyComment();
        post mockPost = mockComment.getPost();

        when(commentService.getCommentByID(eq(commentID))).thenReturn(Optional.of(mockComment));

        mockMvc.perform(MockMvcRequestBuilders.post("/deleteComment")
                        .param("commentID", Integer.toString(commentID)))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrlPattern("post*"));
    }

    @Test
    public void updateCommentShouldRedirectToPostView() throws Exception {
        int commentID = generalFunctions.randNum();
        String editedContent = "Edited Content";
        comment mockComment = generalFunctions.dummyComment();
        post mockPost = mockComment.getPost();

        when(commentService.getCommentByID(eq(commentID))).thenReturn(Optional.of(mockComment));

        mockMvc.perform(MockMvcRequestBuilders.post("/updateComment")
                        .param("commentID", Integer.toString(commentID))
                        .param("edited", editedContent))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrlPattern("post*"));
    }
}
