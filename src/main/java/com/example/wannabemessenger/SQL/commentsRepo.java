package com.example.wannabemessenger.SQL;

import com.example.wannabemessenger.objectables.comment;
import com.example.wannabemessenger.objectables.friendRequest;
import com.example.wannabemessenger.objectables.post;
import com.example.wannabemessenger.objectables.user;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface commentsRepo extends JpaRepository<comment,Integer> {
    @Query("SELECT f FROM comment f WHERE f.post = ?1")
    List<comment> findCommentsByPost(post post);
}
