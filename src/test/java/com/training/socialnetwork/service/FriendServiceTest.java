package com.training.socialnetwork.service;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;

import com.training.socialnetwork.entity.Friend;
import com.training.socialnetwork.entity.User;
import com.training.socialnetwork.repository.FriendRepository;
import com.training.socialnetwork.repository.UserRepository;
import com.training.socialnetwork.service.impl.FriendService;
import com.training.socialnetwork.util.constant.Constant;
import com.training.socialnetwork.util.exception.CustomException;

@ExtendWith(MockitoExtension.class)
public class FriendServiceTest {

	@InjectMocks
	private FriendService friendService;

	@Mock
	private FriendRepository friendRepository;

	@Mock
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
		friend1.setSentUser(user1);
		friend1.setReceivedUser(user2);
		friend1.setStatus(1);

		Friend friend2 = new Friend();
		friend2.setFriendId(2);
		friend2.setSentUser(user3);
		friend2.setReceivedUser(user1);
		friend2.setStatus(1);

		List<Friend> friendList = new ArrayList<Friend>();
		friendList.add(friend1);
		friendList.add(friend2);

		Page<Friend> page = new PageImpl<Friend>(friendList);
		when(friendRepository.findAllFriendByUserIdAndStatus(1, 1, null)).thenReturn(page);

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
		friend1.setSentUser(user1);
		friend1.setReceivedUser(user2);
		friend1.setStatus(0);

		when(userRepository.findById(any())).thenReturn(Optional.of(user1));
		when(userRepository.findById(any())).thenReturn(Optional.of(user2));
		when(friendRepository.findFriendBySentUserAndReceivedUser(userId1, userId2)).thenReturn(null);
		when(friendRepository.save(any())).thenReturn(friend1);

		friendService.createFriendRequest(userId1, userId2);
	}
	
	@Test
	public void createFriendRequestFail() throws Exception {
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

		when(userRepository.findById(any())).thenReturn(Optional.of(user1));
		when(userRepository.findById(any())).thenReturn(Optional.empty());

		assertThrows(CustomException.class, () -> friendService.createFriendRequest(userId1, userId2));
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
		friend1.setSentUser(user1);
		friend1.setReceivedUser(user2);
		friend1.setStatus(1);

		when(userRepository.findById(any())).thenReturn(Optional.of(user1));
		when(userRepository.findById(any())).thenReturn(Optional.of(user2));
		when(friendRepository.findFriendRequestByUserId(userId1, userId2)).thenReturn(null);

		assertThrows(CustomException.class, () -> friendService.acceptFriendRequest(userId1, userId2));
	}
	
	@Test
	public void acceptFriendRequestFail2() throws Exception {
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

		when(userRepository.findById(any())).thenReturn(Optional.of(user1));
		when(userRepository.findById(any())).thenReturn(Optional.empty());

		assertThrows(CustomException.class, () -> friendService.acceptFriendRequest(userId1, userId2));
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
		friend1.setSentUser(user1);
		friend1.setReceivedUser(user2);
		friend1.setStatus(0);

		when(userRepository.findById(any())).thenReturn(Optional.of(user1));
		when(userRepository.findById(any())).thenReturn(Optional.of(user2));
		when(friendRepository.findFriendRequestByUserId(userId1, userId2)).thenReturn(friend1);
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
		friend1.setSentUser(user1);
		friend1.setReceivedUser(user2);
		friend1.setStatus(0);

		when(userRepository.findById(any())).thenReturn(Optional.of(user1));
		when(userRepository.findById(any())).thenReturn(Optional.of(user2));
		when(friendRepository.findFriendRequestByUserId(userId1, userId2)).thenReturn(friend1);
		when(friendRepository.save(any())).thenReturn(friend1);

		friendService.refuseFriendRequest(userId1, userId2);
	}
	
	@Test
	public void refuseFriendRequestFail() throws Exception {
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
		friend1.setSentUser(user1);
		friend1.setReceivedUser(user2);
		friend1.setStatus(1);

		when(userRepository.findById(any())).thenReturn(Optional.of(user1));
		when(userRepository.findById(any())).thenReturn(Optional.of(user2));
		when(friendRepository.findFriendRequestByUserId(userId1, userId2)).thenReturn(null);

		assertThrows(CustomException.class, () -> friendService.refuseFriendRequest(userId1, userId2));
	}
	
	@Test
	public void refuseFriendRequestFail2() throws Exception {
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

		when(userRepository.findById(any())).thenReturn(Optional.of(user1));
		when(userRepository.findById(any())).thenReturn(Optional.empty());

		assertThrows(CustomException.class, () -> friendService.refuseFriendRequest(userId1, userId2));
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
		friend1.setSentUser(user1);
		friend1.setReceivedUser(user2);
		friend1.setStatus(1);

		when(userRepository.findById(any())).thenReturn(Optional.of(user1));
		when(userRepository.findById(any())).thenReturn(Optional.of(user2));
		when(friendRepository.findFriendByUserIdAndStatus(userId1, userId2, Constant.FRIENDED_STATUS)).thenReturn(friend1);
		when(friendRepository.save(any())).thenReturn(friend1);

		friendService.unfriend(userId1, userId2);
	}

	@Test
	public void unfriendRequestFail() throws Exception {
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
		friend1.setSentUser(user1);
		friend1.setReceivedUser(user2);
		friend1.setStatus(1);

		when(userRepository.findById(any())).thenReturn(Optional.of(user1));
		when(userRepository.findById(any())).thenReturn(Optional.of(user2));
		
		assertThrows(CustomException.class, () -> friendService.unfriend(userId1, userId2));
	}
	
	@Test
	public void unfriendRequestFail2() throws Exception {
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

		when(userRepository.findById(any())).thenReturn(Optional.of(user1));
		when(userRepository.findById(any())).thenReturn(Optional.empty());

		assertThrows(CustomException.class, () -> friendService.unfriend(userId1, userId2));
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
		friend1.setSentUser(user2);
		friend1.setReceivedUser(user1);
		friend1.setStatus(0);
		
		List<Friend> friendList = new ArrayList<Friend>();
		friendList.add(friend1);

		when(friendRepository.findAllFriendRequest(anyInt(), anyInt(), any())).thenReturn(friendList);

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
		friend1.setSentUser(user1);
		friend1.setReceivedUser(user2);
		friend1.setStatus(0);

		when(userRepository.findById(any())).thenReturn(Optional.of(user1));
		when(userRepository.findById(any())).thenReturn(Optional.of(user2));
		when(friendRepository.findFriendRequestByUserId(userId1, userId2)).thenReturn(friend1);

		friendService.removeFriendRequest(userId1, userId2);
	}
	
	@Test
	public void removeFriendRequestFail() throws Exception {
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
		friend1.setSentUser(user1);
		friend1.setReceivedUser(user2);
		friend1.setStatus(1);

		when(userRepository.findById(any())).thenReturn(Optional.of(user1));
		when(userRepository.findById(any())).thenReturn(Optional.of(user2));
		when(friendRepository.findFriendRequestByUserId(userId1, userId2)).thenReturn(null);

		assertThrows(CustomException.class, () -> friendService.removeFriendRequest(userId1, userId2));
	}
	
	@Test
	public void removeFriendRequestFail2() throws Exception {
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

		when(userRepository.findById(any())).thenReturn(Optional.of(user1));
		when(userRepository.findById(any())).thenReturn(Optional.empty());

		assertThrows(CustomException.class, () -> friendService.removeFriendRequest(userId1, userId2));
	}
}
