package com.example.wannabemessenger.SQL;

import com.example.wannabemessenger.objectables.post;
import org.springframework.data.jpa.repository.JpaRepository;

public interface postsRepo extends JpaRepository<post, Integer> {
}
