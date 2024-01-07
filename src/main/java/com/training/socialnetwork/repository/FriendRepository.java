package com.training.socialnetwork.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.training.socialnetwork.entity.Friend;

@Repository
public interface FriendRepository extends JpaRepository<Friend, Integer>{
	
	@Query(value = "" + 
			"select * from friend as f " + 
			"where f.user_id1 = :userId or f.user_id2 = :userId " +
			"and f.status = :status", nativeQuery = true)
	List<Friend> findAllFriendByUserIdAndStatus(@Param(value = "userId") int userId, @Param(value = "status") int status);

	@Query(value = "" + 
				"select * from friend as f " + 
				"where (f.user_id1 = :userId1 and f.user_id2 = :userId2) " + 
				"or (f.user_id1 = :userId2 and f.user_id2 = :userId1) ", nativeQuery = true)
	Friend findFriendByUser1AndUser2(@Param(value = "userId1") int userId1, @Param(value = "userId1") int userId2);

	@Query(value = "" + 
			"select * from friend as f " + 
			"where ((f.user_id1 = :userId1 and f.user_id2 = :userId2) " + 
			"or (f.user_id1 = :userId2 and f.user_id2 = :userId1)) " + 
			"and f.status = :status ", nativeQuery = true)
	Friend findFriendByUserIdAndStatus(@Param(value = "userId1") int userId1, @Param(value = "userId2") int userId2, @Param(value = "status") int status);
	
	@Query(value = "" + 
			"select * from friend as f " + 
			"where f.user_id1 = :userId " + 
			"or f.user_id2 = :userId ", nativeQuery = true)
	List<Friend> findAllByUserId(int userId);
}
