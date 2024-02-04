package com.training.socialnetwork.repository;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.training.socialnetwork.entity.Post;

@Repository
public interface PostRepository extends JpaRepository<Post, Integer>{

	@Query(value = "" + 
				"select * from posts as p " + 
				"where p.user_id in ( :friendUserId) " + 
				"and p.delete_flg = 0 " + 
				"order by p.update_date ", nativeQuery = true)
	List<Post> findAllByUserId(@Param(value = "friendUserId") List<Integer> friendUserId, Pageable paging);
	
	@Query(value = "" + 
				"select count(p.post_id) as post " + 
				"from users as u " + 
				"inner join posts p on u.user_id = p.user_id " +
				"where u.user_id = :userId " + 
				"and p.create_date >= :dateStart and p.create_date <= :dateEnd " +
				"and p.delete_flg = 0 ", nativeQuery = true)
	int countPost(@Param(value = "userId") int userId, @Param(value = "dateStart") LocalDate dateStart, @Param(value = "dateEnd") LocalDate dateEnd);
}
