package com.training.socialnetwork.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.training.socialnetwork.entity.Comment;

@Repository
public interface CommentRepository extends JpaRepository<Comment, Integer>{

}
