package com.training.socialnetwork.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.training.socialnetwork.entity.PostEntity;

public interface PostRepository extends JpaRepository<PostEntity, Integer>{

}
