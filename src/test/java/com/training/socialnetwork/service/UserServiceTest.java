package com.training.socialnetwork.service;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import java.time.LocalDate;
import java.time.temporal.TemporalField;
import java.time.temporal.WeekFields;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Optional;
import java.util.UUID;

import javax.transaction.Transactional;

import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.junit4.SpringRunner;

import com.training.socialnetwork.dto.request.user.UserRegisterDto;
import com.training.socialnetwork.dto.request.user.UserUpdateDto;
import com.training.socialnetwork.dto.response.user.UserDetailDto;
import com.training.socialnetwork.dto.response.user.UserReportDto;
import com.training.socialnetwork.entity.Friend;
import com.training.socialnetwork.entity.Role;
import com.training.socialnetwork.entity.User;
import com.training.socialnetwork.repository.CommentRepository;
import com.training.socialnetwork.repository.FriendRepository;
import com.training.socialnetwork.repository.LikeRepository;
import com.training.socialnetwork.repository.PostRepository;
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
	
	@MockBean
	private PostRepository postRepository;
	
	@MockBean
	private LikeRepository likeRepository;
	
	@MockBean
	private CommentRepository commentRepository;
	
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
		MockMultipartFile avatar =
                new MockMultipartFile("data2", "filename2.jpg", "multipart/form-data", "some xml".getBytes());
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
		
		userService.updateInfo(userUpdateDto, avatar, 1, 1);
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
	public void getReportUserSuccess() {
		LocalDate date = LocalDate.now();
		TemporalField fieldISO = WeekFields.of(Locale.FRANCE).dayOfWeek();
		LocalDate dateStart = date.with(fieldISO, 1);
		LocalDate dateEnd = date.with(fieldISO, 7);
		
		UserReportDto userReportDto = new UserReportDto();
		int userId = 1;
		int postCount = 1;
		int commentCount = 2;
		int friendCount = 3;
		int likeCount = 4;
		
		when(postRepository.countPost(userId, dateStart, dateEnd)).thenReturn(postCount);
		when(commentRepository.countComment(userId, dateStart, dateEnd)).thenReturn(commentCount);
		when(friendRepository.countFriend(userId, dateStart, dateEnd)).thenReturn(friendCount);
		when(likeRepository.countLike(userId, dateStart, dateEnd)).thenReturn(likeCount);
		
		userReportDto.setPostCount(postCount);
		userReportDto.setCommentCount(commentCount);
		userReportDto.setFriendCount(friendCount);
		userReportDto.setLikeCount(likeCount);
		
		userService.getReportUser(userId);
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
		user1.setCreateDate(new Date());
		
		when(userRepository.findByToken(token)).thenReturn(user1);
		String newPassword = "654321";
		
		when(userRepository.save(any())).thenReturn(user1);
		
		userService.resetPassword(token, newPassword);
	}
}
