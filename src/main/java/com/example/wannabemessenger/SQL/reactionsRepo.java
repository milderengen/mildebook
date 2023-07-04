package com.example.wannabemessenger.SQL;

import com.example.wannabemessenger.objectables.reaction;
import org.springframework.data.jpa.repository.JpaRepository;

public interface reactionsRepo extends JpaRepository<reaction,Integer> {
}
