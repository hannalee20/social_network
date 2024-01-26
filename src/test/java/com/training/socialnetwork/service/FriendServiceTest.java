package com.training.socialnetwork.service;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;

import com.training.socialnetwork.entity.Friend;
import com.training.socialnetwork.entity.User;
import com.training.socialnetwork.repository.FriendRepository;
import com.training.socialnetwork.repository.UserRepository;
import com.training.socialnetwork.service.impl.FriendService;
import com.training.socialnetwork.util.constant.Constant;

@RunWith(SpringRunner.class)
@SpringBootTest
public class FriendServiceTest {

	@Autowired
	private FriendService friendService;

	@MockBean
	private FriendRepository friendRepository;

	@MockBean
	private UserRepository userRepository;

	@Test
	public void findAllFriendWithStatusSuccess() {
		int userId = 1;
		User user1 = new User();
		user1.setUserId(1);
		user1.setUsername("test1");
		user1.setPassword("123456");
		user1.setEmail("test1@gmail.com");
		user1.setGender(0);
		user1.setBirthDate(new Date());
		user1.setAddress("Hanoi");

		User user2 = new User();
		user2.setUserId(2);
		user2.setUsername("test2");
		user2.setPassword("123456");
		user2.setEmail("test2@gmail.com");
		user2.setGender(1);
		user2.setBirthDate(new Date());
		user2.setAddress("HCM");

		User user3 = new User();
		user3.setUserId(3);
		user3.setUsername("test3");
		user3.setPassword("123456");
		user3.setEmail("test3@gmail.com");
		user3.setGender(1);
		user3.setBirthDate(new Date());
		user3.setAddress("Hanoi");

		Friend friend1 = new Friend();
		friend1.setFriendId(1);
		friend1.setUser1(user1);
		friend1.setUser2(user2);
		friend1.setStatus(1);

		Friend friend2 = new Friend();
		friend2.setFriendId(2);
		friend2.setUser1(user1);
		friend2.setUser2(user3);
		friend2.setStatus(1);

		List<Friend> friendList = new ArrayList<Friend>();
		friendList.add(friend1);
		friendList.add(friend2);

		when(friendRepository.findAllFriendByUserIdAndStatus(1, 1, null)).thenReturn(friendList);

		friendService.findAllFriendWithStatus(userId, null);
	}

	@Test
	public void createFriendRequestSuccess() throws Exception {
		int userId1 = 1;
		int userId2 = 2;
		User user1 = new User();
		user1.setUserId(1);
		user1.setUsername("test1");
		user1.setPassword("123456");
		user1.setEmail("test1@gmail.com");
		user1.setGender(0);
		user1.setBirthDate(new Date());
		user1.setAddress("Hanoi");

		User user2 = new User();
		user2.setUserId(2);
		user2.setUsername("test2");
		user2.setPassword("123456");
		user2.setEmail("test2@gmail.com");
		user2.setGender(1);
		user2.setBirthDate(new Date());
		user2.setAddress("HCM");

		Friend friend1 = new Friend();
		friend1.setFriendId(1);
		friend1.setUser1(user1);
		friend1.setUser2(user2);
		friend1.setStatus(0);

		when(userRepository.findById(any())).thenReturn(Optional.of(user1));
		when(userRepository.findById(any())).thenReturn(Optional.of(user2));
		when(friendRepository.findFriendByUser1AndUser2(userId1, userId2)).thenReturn(null);
		when(friendRepository.save(any())).thenReturn(friend1);

		friendService.createFriendRequest(userId1, userId2);
	}

	@Test
	public void acceptFriendRequestFail() throws Exception {
		int userId1 = 1;
		int userId2 = 2;
		User user1 = new User();
		user1.setUserId(1);
		user1.setUsername("test1");
		user1.setPassword("123456");
		user1.setEmail("test1@gmail.com");
		user1.setGender(0);
		user1.setBirthDate(new Date());
		user1.setAddress("Hanoi");

		User user2 = new User();
		user2.setUserId(2);
		user2.setUsername("test2");
		user2.setPassword("123456");
		user2.setEmail("test2@gmail.com");
		user2.setGender(1);
		user2.setBirthDate(new Date());
		user2.setAddress("HCM");

		Friend friend1 = new Friend();
		friend1.setFriendId(1);
		friend1.setUser1(user1);
		friend1.setUser2(user2);
		friend1.setStatus(1);

		when(userRepository.findById(any())).thenReturn(Optional.of(user1));
		when(userRepository.findById(any())).thenReturn(Optional.of(user2));
		when(friendRepository.findFriendByUserIdAndStatus(userId1, userId2, Constant.FRIEND_REQUEST)).thenReturn(friend1);
		when(friendRepository.save(any())).thenReturn(friend1);

		friendService.acceptFriendRequest(userId1, userId2);
	}
	
	@Test
	public void acceptFriendRequestSuccess() throws Exception {
		int userId1 = 1;
		int userId2 = 2;
		User user1 = new User();
		user1.setUserId(1);
		user1.setUsername("test1");
		user1.setPassword("123456");
		user1.setEmail("test1@gmail.com");
		user1.setGender(0);
		user1.setBirthDate(new Date());
		user1.setAddress("Hanoi");

		User user2 = new User();
		user2.setUserId(2);
		user2.setUsername("test2");
		user2.setPassword("123456");
		user2.setEmail("test2@gmail.com");
		user2.setGender(1);
		user2.setBirthDate(new Date());
		user2.setAddress("HCM");

		Friend friend1 = new Friend();
		friend1.setFriendId(1);
		friend1.setUser1(user1);
		friend1.setUser2(user2);
		friend1.setStatus(0);

		when(userRepository.findById(any())).thenReturn(Optional.of(user1));
		when(userRepository.findById(any())).thenReturn(Optional.of(user2));
		when(friendRepository.findFriendByUserIdAndStatus(userId1, userId2, Constant.FRIEND_REQUEST)).thenReturn(friend1);
		when(friendRepository.save(any())).thenReturn(friend1);

		friendService.acceptFriendRequest(userId1, userId2);
	}

	@Test
	public void refuseFriendRequestSuccess() throws Exception {
		int userId1 = 1;
		int userId2 = 2;
		User user1 = new User();
		user1.setUserId(1);
		user1.setUsername("test1");
		user1.setPassword("123456");
		user1.setEmail("test1@gmail.com");
		user1.setGender(0);
		user1.setBirthDate(new Date());
		user1.setAddress("Hanoi");

		User user2 = new User();
		user2.setUserId(2);
		user2.setUsername("test2");
		user2.setPassword("123456");
		user2.setEmail("test2@gmail.com");
		user2.setGender(1);
		user2.setBirthDate(new Date());
		user2.setAddress("HCM");

		Friend friend1 = new Friend();
		friend1.setFriendId(1);
		friend1.setUser1(user1);
		friend1.setUser2(user2);
		friend1.setStatus(0);

		when(userRepository.findById(any())).thenReturn(Optional.of(user1));
		when(userRepository.findById(any())).thenReturn(Optional.of(user2));
		when(friendRepository.findFriendByUserIdAndStatus(userId1, userId2, Constant.FRIEND_REQUEST)).thenReturn(friend1);
		when(friendRepository.save(any())).thenReturn(friend1);

		friendService.refuseFriendRequest(userId1, userId2);
	}

	@Test
	public void unfriendRequestSuccess() throws Exception {
		int userId1 = 1;
		int userId2 = 2;
		User user1 = new User();
		user1.setUserId(1);
		user1.setUsername("test1");
		user1.setPassword("123456");
		user1.setEmail("test1@gmail.com");
		user1.setGender(0);
		user1.setBirthDate(new Date());
		user1.setAddress("Hanoi");

		User user2 = new User();
		user2.setUserId(2);
		user2.setUsername("test2");
		user2.setPassword("123456");
		user2.setEmail("test2@gmail.com");
		user2.setGender(1);
		user2.setBirthDate(new Date());
		user2.setAddress("HCM");

		Friend friend1 = new Friend();
		friend1.setFriendId(1);
		friend1.setUser1(user1);
		friend1.setUser2(user2);
		friend1.setStatus(1);

		when(userRepository.findById(any())).thenReturn(Optional.of(user1));
		when(userRepository.findById(any())).thenReturn(Optional.of(user2));
		when(friendRepository.findFriendByUserIdAndStatus(userId1, userId2, Constant.FRIENDED_STATUS)).thenReturn(friend1);
		when(friendRepository.save(any())).thenReturn(friend1);

		friendService.unfriend(userId1, userId2);
	}

	@Test
	public void findAllAddFriendRequestSuccess() {
		int userId = 1;

		User user1 = new User();
		user1.setUserId(1);
		user1.setUsername("test1");
		user1.setPassword("123456");
		user1.setEmail("test1@gmail.com");
		user1.setGender(0);
		user1.setBirthDate(new Date());
		user1.setAddress("Hanoi");

		User user2 = new User();
		user2.setUserId(2);
		user2.setUsername("test2");
		user2.setPassword("123456");
		user2.setEmail("test2@gmail.com");
		user2.setGender(1);
		user2.setBirthDate(new Date());
		user2.setAddress("HCM");

		User user3 = new User();
		user3.setUserId(3);
		user3.setUsername("test3");
		user3.setPassword("123456");
		user3.setEmail("test3@gmail.com");
		user3.setGender(1);
		user3.setBirthDate(new Date());
		user3.setAddress("Hanoi");

		Friend friend1 = new Friend();
		friend1.setFriendId(1);
		friend1.setUser1(user1);
		friend1.setUser2(user2);
		friend1.setStatus(0);

		Friend friend2 = new Friend();
		friend2.setFriendId(2);
		friend2.setUser1(user1);
		friend2.setUser2(user3);
		friend2.setStatus(0);

		List<Friend> friendList = new ArrayList<Friend>();
		friendList.add(friend1);
		friendList.add(friend2);

		when(friendRepository.findAllFriendRequest(userId, Constant.FRIEND_REQUEST, null)).thenReturn(friendList);

		friendService.findAllAddFriendRequest(userId, null);
	}

	@Test
	public void removeFriendRequestSuccess() throws Exception {
		int userId1 = 1;
		int userId2 = 2;

		User user1 = new User();
		user1.setUserId(1);
		user1.setUsername("test1");
		user1.setPassword("123456");
		user1.setEmail("test1@gmail.com");
		user1.setGender(0);
		user1.setBirthDate(new Date());
		user1.setAddress("Hanoi");

		User user2 = new User();
		user2.setUserId(2);
		user2.setUsername("test2");
		user2.setPassword("123456");
		user2.setEmail("test2@gmail.com");
		user2.setGender(1);
		user2.setBirthDate(new Date());
		user2.setAddress("HCM");

		Friend friend1 = new Friend();
		friend1.setFriendId(1);
		friend1.setUser1(user1);
		friend1.setUser2(user2);
		friend1.setStatus(0);

		when(userRepository.findById(any())).thenReturn(Optional.of(user1));
		when(userRepository.findById(any())).thenReturn(Optional.of(user2));
		when(friendRepository.findFriendByUserIdAndStatus(userId1, userId2 , Constant.FRIEND_REQUEST)).thenReturn(friend1);

		friendService.removeFriendRequest(userId1, userId2);
	}
}
