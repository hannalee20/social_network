package com.training.socialnetwork.repository;

import java.time.LocalDate;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.training.socialnetwork.entity.Like;
import com.training.socialnetwork.entity.Post;
import com.training.socialnetwork.entity.User;

@Repository
public interface LikeRepository extends JpaRepository<Like, Integer>{

	Like findByPostAndUser(Post postEntity,User userEntity);
	
	@Query(value = "" + 
			"select count(l.like_id) as likes " + 
			"from users as u " + 
			"inner join posts p on u.user_id = p.user_id " + 
			"inner join likes l on p.post_id = l.post_id " +
			"where u.user_id = :userId " + 
			"and l.delete_flg = 0 " + 
			"and l.create_date between ':dateStart' and ':dateEnd' ", nativeQuery = true)
	int countLike(@Param(value = "userId") int userId, @Param(value = "dateStart") LocalDate dateStart, @Param(value = "dateEnd") LocalDate dateEnd);
}
