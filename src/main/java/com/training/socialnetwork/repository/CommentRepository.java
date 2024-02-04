package com.training.socialnetwork.repository;

import java.time.LocalDate;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.training.socialnetwork.entity.Comment;

@Repository
public interface CommentRepository extends JpaRepository<Comment, Integer>{

	@Query(value = "" + 
			"select count(c.comment_id) as comment " + 
			"from posts as p " + 
			"inner join comments c on p.post_id = c.post_id " +
			"where p.user_id = :userId " + 
			"and c.user_id <> :userId " + 
			"and c.create_date >= :dateStart and c.create_date <= :dateEnd " + 
			"and c.delete_flg = 0 ", nativeQuery = true)
	int countComment(@Param(value = "userId") int userId, @Param(value = "dateStart") LocalDate dateStart, @Param(value = "dateEnd") LocalDate dateEnd);
}
