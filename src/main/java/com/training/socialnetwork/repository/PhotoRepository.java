package com.training.socialnetwork.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.training.socialnetwork.entity.Photo;

@Repository
public interface PhotoRepository extends JpaRepository<Photo, Integer>{

	@Query(value = "" + 
			"select * from photos as p " + 
			"where p.photo_id = :postId " +
			"and p.delete_flg = 0 ", nativeQuery = true)
	List<Photo> findByPostId(@Param(value = "postId") int postId);
	
	@Query(value = "" + 
			"select * from photos as p " +
			"where p.user_id = :userId " +
			"and p.avatar = 1 " + 
			"and p.delete_flg = 0 ", nativeQuery = true)
	Photo findAvatarByUserId(@Param(value = "userId") int userId);
	
	@Query(value = "" + 
			"select * from photos as p " + 
			"where p.user_id = :userId " + 
			"and p.delete_flg = 0 ", nativeQuery = true)
	Page<Photo> findAllPhotosByUserId(int userId, Pageable paging);
}
