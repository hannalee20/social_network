package com.training.socialnetwork.service;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import javax.transaction.Transactional;

import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;

import com.training.socialnetwork.dto.request.user.UserRegisterDto;
import com.training.socialnetwork.dto.request.user.UserUpdateDto;
import com.training.socialnetwork.dto.response.user.UserDetailDto;
import com.training.socialnetwork.entity.Friend;
import com.training.socialnetwork.entity.Role;
import com.training.socialnetwork.entity.User;
import com.training.socialnetwork.repository.FriendRepository;
import com.training.socialnetwork.repository.RoleRepository;
import com.training.socialnetwork.repository.UserRepository;
import com.training.socialnetwork.service.impl.UserService;
import com.training.socialnetwork.util.constant.Constant;
import com.training.socialnetwork.util.mapper.ObjectMapper;

//@ExtendWith(MockitoExtension.class)
@RunWith(SpringRunner.class)
@SpringBootTest
//@AutoConfigureMockMvc
@Transactional
public class UserServiceTest {

	@Autowired
	private UserService userService;
	
	@Autowired
	private ObjectMapper objectMapper;
	
	@Autowired
	private ModelMapper modelMapper;
	
	@MockBean
	private UserRepository userRepository;
	
	@MockBean
	private RoleRepository roleRepository;
	
	@MockBean
	private FriendRepository friendRepository;
	
	@Test
	public void createUserSuccess() throws Exception {
		UserRegisterDto userRegisterDto = new UserRegisterDto();
		userRegisterDto.setUsername("test");
		userRegisterDto.setPassword("123456");
		userRegisterDto.setEmail("test@gmail.com");
		
		Role role = new Role();
		role.setRoleId(1);
		role.setName(Constant.ROLE_USER);
		
		User user = new User();
		user.setUserId(1);
		user.setUsername("test");
		user.setPassword("123456");
		user.setEmail("test@gmail.com");
		user.setRole(role);
		user.setCreateDate(new Date());
		user.setUpdateDate(new Date());
		
		when(userRepository.findByUsernameOrEmail(userRegisterDto.getUsername(), userRegisterDto.getEmail())).thenReturn(null);
		when(roleRepository.findByName(Constant.ROLE_USER)).thenReturn(role);
		when(userRepository.save(any())).thenReturn(user);
		
		userService.createUser(userRegisterDto);
	}
	
	@Test
	public void loginUserFail() throws Exception {
		String username = "test";
		String password = "123456";
		
		User user = new User();
		user.setUserId(1);
		user.setUsername("test");
		user.setPassword("123456");
		user.setEmail("test@gmail.com");
		
		when(userRepository.findByUsername(username)).thenReturn(user);
		userService.loginUser(username, password);
	}
	
	@Test
	public void updateInfoSuccess() throws Exception {
		UserUpdateDto userUpdateDto = new UserUpdateDto();
		userUpdateDto.setRealName("Test");
		userUpdateDto.setSex("female");
		userUpdateDto.setAddress("Hanoi");
		
		User userToUpdate = new User();
		userToUpdate.setUserId(1);
		userToUpdate.setUsername("test");
		userToUpdate.setEmail("test@gmail.com");
		
		when(userRepository.findById(1)).thenReturn(Optional.of(userToUpdate));
		
		objectMapper.copyProperties(userUpdateDto, userToUpdate);
		
		when(userRepository.save(any())).thenReturn(userToUpdate);
		
		userService.updateInfo(userUpdateDto, null, 1, 1);
	}
	
	@Test
	public void getInfoSuccess() throws Exception {
		User user = new User();
		user.setUserId(1);
		user.setUsername("test");
		user.setPassword("123456");
		user.setEmail("test@gmail.com");
		user.setGender(0);
		user.setBirthDate(new Date());
		user.setAddress("Hanoi");
		
		UserDetailDto userDetailDto = new UserDetailDto();
		modelMapper.map(user, userDetailDto);
		userDetailDto.setGender(Constant.MALE);
		
		when(userRepository.findById(1)).thenReturn(Optional.of(user));
		
		userService.getInfo(1);
	}
	
	@Test
	public void searchUserSuccess() {
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
		
		List<User> userList = new ArrayList<>();
		userList.add(user1);
		userList.add(user2);
//		userList.add(user3);
		
		Friend friend = new Friend();
		friend.setFriendId(1);
		friend.setUser1(user1);
		friend.setUser2(user3);
		friend.setStatus(1);
		
		List<Friend> friendList = new ArrayList<Friend>();
		friendList.add(friend);
		
		String keyword = "test";
		
		when(userRepository.findAllUserByKeyword(3, keyword)).thenReturn(userList);
		when(friendRepository.findAllByUserId(3)).thenReturn(friendList);
		
		userService.searchUser(3, keyword);
	}
	
	@Test
	public void getForgotPasswordSuccess() throws Exception {
		User user1 = new User();
		user1.setUserId(1);
		user1.setUsername("test1");
		user1.setPassword("123456");
		user1.setEmail("test1@gmail.com");
		user1.setGender(0);
		user1.setBirthDate(new Date());
		user1.setAddress("Hanoi");
		
		String email = "test1@gmail.com";
		
		when(userRepository.findByEmail(any())).thenReturn(user1);
		String token = UUID.randomUUID().toString();
		user1.setToken(token);
		user1.setTokenCreateDate(new Date());
		when(userRepository.save(any())).thenReturn(user1);
		
		userService.forgotPassword(email);
	}
	
	@Test
	public void resetPasswordSuccess() throws Exception {
		String token = "testToken";
		User user1 = new User();
		user1.setUserId(1);
		user1.setUsername("test1");
		user1.setPassword("123456");
		user1.setEmail("test1@gmail.com");
		user1.setToken(token);
		user1.setGender(0);
		user1.setBirthDate(new Date());
		user1.setAddress("Hanoi");
		user1.setTokenCreateDate(new Date());
		
		when(userRepository.findByToken(token)).thenReturn(user1);
		String newPassword = "654321";
		user1.setToken(null);
		user1.setTokenCreateDate(null);
		user1.setUpdateDate(new Date());
		
		when(userRepository.save(any())).thenReturn(user1);
		
		userService.resetPassword(token, newPassword);
	}
}
