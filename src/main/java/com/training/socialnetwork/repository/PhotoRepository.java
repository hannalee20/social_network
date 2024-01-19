package com.training.socialnetwork.repository;

import java.util.List;

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
	
}
