package com.training.socialnetwork.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.training.socialnetwork.entity.User;

@Repository
public interface UserRepository extends JpaRepository<User, Integer>{
	
	public User findByUsername(String username);

	@Query(value = "" + 
				"select u from User as u " + 
				"where u.userId <> :userId " + 
				"and (lower(u.username) like concat('%', :keyword, '%') " + 
				"or lower(u.realName) like concat('%', :keyword, '%')) ")
	List<User> findAllUserLike(@Param(value = "userId") int userId, @Param(value = "keyword") String keyword);
}
