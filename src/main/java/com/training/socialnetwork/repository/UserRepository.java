package com.training.socialnetwork.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.training.socialnetwork.entity.UserEntity;

@Repository
public interface UserRepository extends JpaRepository<UserEntity, Integer>{
	
	public UserEntity findByUsername(String username);
	
	@Query("select * from user u where u.username = :username, u.password = :password")
	public UserEntity getUserLogin(@Param("username") String username, @Param("password") String password);
}
