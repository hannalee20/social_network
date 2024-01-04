package com.training.socialnetwork.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.training.socialnetwork.entity.Post;

@Repository
public interface PostRepository extends JpaRepository<Post, Integer>{

	List<Post> findAllByUserUserIdOrderByUpdateDateDesc(@Param(value = "userId") int userId);
}
