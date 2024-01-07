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
				"select p.post_id, p.post_id, p.content, p.photo_url from post as p " + 
				"left join friend f1 on p.user_id = f1.user_id1 " + 
				"left join friend f2 on p.user_id = f2.user_id2 " + 
				"where p.user_id = :userId and f1.status = 1 and f2.status = 1" + 
				"order by p.update_date ", nativeQuery = true)
	List<Post> findAllByUserId(@Param(value = "userId") int userId);
}
