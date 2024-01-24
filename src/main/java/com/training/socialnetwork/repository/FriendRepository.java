package com.training.socialnetwork.repository;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.training.socialnetwork.entity.Friend;

@Repository
public interface FriendRepository extends JpaRepository<Friend, Integer>{
	
	@Query(value = "" + 
			"select * from friends as f " + 
			"where f.user_id1 = :userId or f.user_id2 = :userId " +
			"and f.status = :status ", nativeQuery = true)
	List<Friend> findAllFriendByUserIdAndStatus(@Param(value = "userId") int userId, @Param(value = "status") int status, Pageable paging);

	@Query(value = "" + 
			"select * from friends as f " + 
			"where f.user_id2 = :userId " +
			"and f.status = :status ", nativeQuery = true)
	List<Friend> findAllFriendRequest(@Param(value = "userId") int userId, @Param(value = "status") int status, Pageable paging);

	
	@Query(value = "" + 
			"select * from friends as f " + 
			"where (f.user_id1 = :userId1 and f.user_id2 = :userId2) " + 
			"or (f.user_id1 = :userId2 and f.user_id2 = :userId1) ", nativeQuery = true)
	Friend findFriendByUser1AndUser2(@Param(value = "userId1") int userId1, @Param(value = "userId2") int userId2);

	@Query(value = "" + 
			"select * from friends as f " + 
			"where ((f.user_id1 = :userId1 and f.user_id2 = :userId2) " + 
			"or (f.user_id1 = :userId2 and f.user_id2 = :userId1)) " + 
			"and f.status = :status ", nativeQuery = true)
	Friend findFriendByUserIdAndStatus(@Param(value = "userId1") int userId1, @Param(value = "userId2") int userId2, @Param(value = "status") int status);
	
	@Query(value = "" + 
			"select * from friends as f " + 
			"where f.user_id1 = :userId " + 
			"or f.user_id2 = :userId ", nativeQuery = true)
	List<Friend> findAllByUserId(int userId);
	
	@Query(value = "" + 
			"select count(f.user_id1 + f.user_id2) as friend " + 
			"from users as u " + 
			"inner join friends f on u.user_id = f.user_id1  or u.user_id = f.user_id2 " +
			"where u.user_id = :userId " + 
			"and f.status = 1  " + 
			"and f.update_date >= :dateStart <= :dateEnd ", nativeQuery = true)
	int countFriend(@Param(value = "userId") int userId, @Param(value = "dateStart") LocalDate dateStart, @Param(value = "dateEnd") LocalDate dateEnd);
}
