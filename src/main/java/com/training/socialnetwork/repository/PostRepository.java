package com.training.socialnetwork.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.training.socialnetwork.entity.Post;

@Repository
public interface PostRepository extends JpaRepository<Post, Integer>{

	@Query(value = "" + 
				"select p from Post as p " + 
				"left join Friend f1 on p.user.userId = f1.user1.userId1 " + 
				"left join Friend f1 on p.user.userId = f1.user2.userId2 " + 
				"where p.user.userId = :userId ")
	List<Post> findAllByUserIdOrderByUpdateDateDesc(@Param(value = "userId") int userId);
}
