package com.training.socialnetwork.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.training.socialnetwork.entity.Like;
import com.training.socialnetwork.entity.Post;
import com.training.socialnetwork.entity.User;

@Repository
public interface LikeRepository extends JpaRepository<Like, Integer>{

	Like findByPostAndUser(Post postEntity,User userEntity);
}
