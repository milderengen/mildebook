package com.example.wannabemessenger.SQL;

import com.example.wannabemessenger.objectables.chat;
import org.springframework.data.jpa.repository.JpaRepository;

public interface chatsRepo extends JpaRepository<chat, Integer> {

}
