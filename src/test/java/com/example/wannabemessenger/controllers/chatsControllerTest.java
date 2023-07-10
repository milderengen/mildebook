package com.example.wannabemessenger.controllers;
import com.example.wannabemessenger.services.chatsService;
import jakarta.servlet.http.HttpSession;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.test.web.servlet.MockMvc;
import com.example.wannabemessenger.services.*;
import com.example.wannabemessenger.generalFunctions;
import com.example.wannabemessenger.objectables.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
public class chatsControllerTest {
    private MockMvc mockMvc;

    @MockBean
    private chatsService chatsService;

    @MockBean
    private messagesService messagesService;

    @MockBean
    private usersService usersService;

    @MockBean
    private generalFunctions generalFunctions;

    @Test
    public void chatShouldReturnChatViewWhenUserIsPartOfChat() throws Exception {
        user me = generalFunctions.dummmyUser();
        user second = generalFunctions.dummmyUser();
        chat testChat = generalFunctions.dummyChat();
        testChat.setChatter1(me);
        testChat.setChatter2(second);
        List<message> messages = testChat.getMessages();
        MockHttpSession session = new MockHttpSession();

        session.setAttribute("userID", me.getId());

        when(generalFunctions.myUserFromSession(any(HttpSession.class), any(usersService.class))).thenReturn(me);
        when(chatsService.getChatByID(eq(testChat.getId()))).thenReturn(Optional.of(testChat));
        when(messagesService.getMessagesByChatID(eq(testChat.getId()))).thenReturn(messages);

        mockMvc.perform(get("/chat")
                        .session(session)
                        .param("chatID", Integer.toString(testChat.getId())))
                .andExpect(status().isOk())
                .andExpect(model().attribute("messages", messages))
                .andExpect(model().attribute("me", me))
                .andExpect(model().attribute("friend", second))
                .andExpect(view().name("chat"));
    }

    @Test
    public void chatShouldReturnBadRequestViewWhenUserIsNotPartOfChat() throws Exception {
        int chatID = 1;
        int userID = 1;
        chat testChat = generalFunctions.dummyChat(); // initialize with appropriate values
        MockHttpSession session = new MockHttpSession();

        session.setAttribute("userID", userID);

        when(generalFunctions.myUserFromSession(any(HttpSession.class), any(usersService.class))).thenReturn(testChat.getChatter1());
        when(chatsService.getChatByID(eq(chatID))).thenReturn(Optional.of(testChat));

        mockMvc.perform(get("/chat")
                        .session(session)
                        .param("chatID", Integer.toString(chatID)))
                .andExpect(status().isOk())
                .andExpect(model().attribute("error403", "forbidden"))
                .andExpect(view().name("badRequest"));
    }
}
