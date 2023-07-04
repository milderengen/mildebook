package com.example.wannabemessenger.SQL;

import com.example.wannabemessenger.objectables.user;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface usersRepo extends JpaRepository<user, Integer> {
    List<user> findByNameStartingWith(String searchString);
}
