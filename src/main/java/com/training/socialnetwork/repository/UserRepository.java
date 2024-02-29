package com.training.socialnetwork.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.training.socialnetwork.entity.User;

@Repository
public interface UserRepository extends JpaRepository<User, Integer> {

	@Query(value = "" + 
			"select * from users as u " + 
			"where lower(username) = lower(:username) " + 
			"or email = :email", nativeQuery = true)
	User findByUsernameOrEmail(String username, String email);
	
	@Query(value = "" + 
			"select * from users as u " + 
			"where lower(username) = lower(:username) ", nativeQuery = true)
	User findByUsername(String username);
	
	User findByEmail(String email);
	
	User findByToken(String token);

	@Query(value = "" + "select * from users as u " + "where u.user_id <> :userId "
			+ "and (lower(u.username) like concat('%', :keyword, '%') "
			+ "or lower(u.real_name) like concat('%', :keyword, '%')) ", nativeQuery = true)
	Page<User> findAllUserByKeyword(@Param(value = "userId") int userId, @Param(value = "keyword") String keyword, Pageable paging);

}
