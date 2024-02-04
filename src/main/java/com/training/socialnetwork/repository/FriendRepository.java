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
public interface FriendRepository extends JpaRepository<Friend, Integer> {

	@Query(value = "" + "select * from friends as f "
			+ "where (f.sent_user_id = :userId or f.recieved_user_id = :userId )"
			+ "and f.status = :status ", nativeQuery = true)
	List<Friend> findAllFriendByUserIdAndStatus(@Param(value = "userId") int userId,
			@Param(value = "status") int status, Pageable paging);

	@Query(value = "" + "select f.sent_user_id, f.recieved_user_id from friends as f "
			+ "where (f.sent_user_id = :userId or f.recieved_user_id = :userId )"
			+ "and f.status = 1 ", nativeQuery = true)
	List<Integer> findAllFriendUserId(@Param(value = "userId") int userId);
	
	@Query(value = "" + "select * from friends as f " + "where f.recieved_user_id = :userId "
			+ "and f.status = :status ", nativeQuery = true)
	List<Friend> findAllFriendRequest(@Param(value = "userId") int userId, @Param(value = "status") int status,
			Pageable paging);

	@Query(value = "" + "select * from friends as f "
			+ "where (f.sent_user_id = :sentUserId and f.recieved_user_id = :recievedUserId) "
			+ "or (f.sent_user_id = :recievedUserId and f.recieved_user_id = :sentUserId) ", nativeQuery = true)
	Friend findFriendBySentUserAndRecievedUser(@Param(value = "sentUserId") int sentUserId,
			@Param(value = "recievedUserId") int recievedUserId);

	@Query(value = "" + "select * from friends as f "
			+ "where ((f.sent_user_id = :sentUserId and f.recieved_user_id = :recievedUserId) "
			+ "or (f.sent_user_id = :recievedUserId and f.recieved_user_id = :sentUserId)) "
			+ "and f.status = :status ", nativeQuery = true)
	Friend findFriendByUserIdAndStatus(@Param(value = "sentUserId") int sentUserId,
			@Param(value = "recievedUserId") int recievedUserId, @Param(value = "status") int status);

	@Query(value = "" + "select * from friends as f "
			+ "where f.sent_user_id = :sentUserId and f.recieved_user_id = :recievedUserId "
			+ "and f.status = 0 ", nativeQuery = true)
	Friend findFriendRequestByUserId(@Param(value = "sentUserId") int sentUserId,
			@Param(value = "recievedUserId") int recievedUserId);

	@Query(value = "" + "select * from friends as f " + "where f.sent_user_id = :userId "
			+ "or f.recieved_user_id = :userId ", nativeQuery = true)
	List<Friend> findAllByUserId(int userId);

	@Query(value = "" + "select count(f.sent_user_id + f.recieved_user_id) as friend " + "from users as u "
			+ "inner join friends f on u.user_id = f.sent_user_id  or u.user_id = f.recieved_user_id "
			+ "where u.user_id = :userId " + "and f.status = 1  "
			+ "and f.update_date >= :dateStart and f.update_date <= :dateEnd ", nativeQuery = true)
	int countFriend(@Param(value = "userId") int userId, @Param(value = "dateStart") LocalDate dateStart,
			@Param(value = "dateEnd") LocalDate dateEnd);
}
