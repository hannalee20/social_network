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
			"select f from Friend as f " + 
			"where f.user1.userId = :userId  or f.user2.userId = :userId " +
			"and f.status = :status")
	List<Friend> findAllFriendByUserIdAndStatus(@Param(value = "userId") int userId, @Param(value = "status") int status);

	@Query(value = "" + 
				"select f from Friend as f " + 
				"where (f.user1.userId1 = :userId1 and f.user2.userId2 = :userId2) " + 
				"or (f.user1.userId1 = :userId2 and f.user2.userId2 = :userId1) ")
	Friend findFriendByUser1AndUser2(@Param(value = "userId1") int userId1, @Param(value = "userId1") int userId2);

	@Query(value = "" + 
			"select f from Friend as f " + 
			"where ((f.user1.userId1 = :userId1 and f.user2.userId2 = :userId2) " + 
			"or (f.user1.userId1 = :userId2 and f.user2.userId2 = :userId1)) " + 
			"and f.status = :status ")
	Friend findFriendByUserIdAndStatus(@Param(value = "userId1") int userId1, @Param(value = "userId2") int userId2, @Param(value = "status") int status);
}
