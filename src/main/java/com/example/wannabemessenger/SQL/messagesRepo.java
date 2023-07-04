package com.example.wannabemessenger.SQL;

import com.example.wannabemessenger.objectables.message;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface messagesRepo extends JpaRepository<message, Integer> {
    List<message> findByChatIdOrderByTimestampDesc(int chatId);
}
