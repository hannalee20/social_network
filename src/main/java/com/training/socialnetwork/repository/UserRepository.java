package com.training.socialnetwork.repository;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.training.socialnetwork.entity.User;

@Repository
public interface UserRepository extends JpaRepository<User, Integer> {

	User findByUsername(String username);
	
	User findByEmail(String email);
	
	User findByToken(String token);

	@Query(value = "" + "select * from users as u" + "where u.user_id <> :userId "
			+ "and (lower(u.username) like concat('%', :keyword, '%') "
			+ "or lower(u.real_name) like concat('%', :keyword, '%')) ", nativeQuery = true)
	List<User> findAllUserByKeyword(@Param(value = "userId") int userId, @Param(value = "keyword") String keyword);

	@Query(value = "" + "select count(p.post_id) as post, " + "count(c.comment_id) as comment, "
			+ "count(f1.user_id1 + f2.user_id2) as friend, " + "count(l.like_id) as likes " + "from users as u "
			+ "inner join posts p on u.user_id = p.user_id " + "inner join comments c on u.user_id = c.user_id "
			+ "inner join friends f1 on u.user_id = f1.user_id1 " + "inner join friends f2 on u.user_id = f2.user_id2 "
			+ "inner join likes l on p.post_id = l.post_id " + "where u.user_id = :userId " + "and f1.status = 1 "
			+ "and f2.status = 1 " + "and l.delete_flg = 0 " + "and p.create_date between ':dateStart' and ':dateEnd' "
			+ "and c.create_date between ':dateStart' and ':dateEnd' "
			+ "and f1.update_date between ':dateStart' and ':dateEnd' "
			+ "and f2.update_date between ':dateStart' and ':dateEnd' "
			+ "and l.create_date between ':dateStart' and ':dateEnd' ", nativeQuery = true)
	User getReport(@Param(value = "userId") int userId, @Param(value = "dateStart") LocalDate dateStart,
			@Param(value = "dateEnd") LocalDate dateEnd);
}
