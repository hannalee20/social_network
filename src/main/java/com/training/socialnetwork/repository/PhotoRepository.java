package com.training.socialnetwork.repository;

import java.util.stream.Stream;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.training.socialnetwork.entity.Photo;

@Repository
public interface PhotoRepository extends JpaRepository<Photo, Integer>{

	Stream<Photo> findByPostPostId(int postId);
	
}
