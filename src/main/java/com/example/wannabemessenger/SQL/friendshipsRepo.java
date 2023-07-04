package com.example.wannabemessenger.SQL;

import com.example.wannabemessenger.objectables.friendship;
import com.example.wannabemessenger.objectables.user;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface friendshipsRepo extends JpaRepository<friendship, Integer> {
    List<friendship> findByPerson(user user);
    List<friendship> findByFriend(user user);
}
