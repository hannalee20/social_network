package com.training.socialnetwork.service;

import static org.junit.Assert.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyString;
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

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import com.training.socialnetwork.dto.request.user.UserRegisterRequestDto;
import com.training.socialnetwork.dto.request.user.UserUpdateRequestDto;
import com.training.socialnetwork.dto.response.user.UserDetailResponseDto;
import com.training.socialnetwork.dto.response.user.UserRegisterResponseDto;
import com.training.socialnetwork.dto.response.user.UserReportResponseDto;
import com.training.socialnetwork.dto.response.user.UserSearchResponseDto;
import com.training.socialnetwork.dto.response.user.UserUpdateResponseDto;
import com.training.socialnetwork.entity.Friend;
import com.training.socialnetwork.entity.Photo;
import com.training.socialnetwork.entity.Role;
import com.training.socialnetwork.entity.User;
import com.training.socialnetwork.repository.CommentRepository;
import com.training.socialnetwork.repository.FriendRepository;
import com.training.socialnetwork.repository.LikeRepository;
import com.training.socialnetwork.repository.PhotoRepository;
import com.training.socialnetwork.repository.PostRepository;
import com.training.socialnetwork.repository.RoleRepository;
import com.training.socialnetwork.repository.UserRepository;
import com.training.socialnetwork.service.impl.UserService;
import com.training.socialnetwork.util.constant.Constant;
import com.training.socialnetwork.util.exception.CustomException;
import com.training.socialnetwork.util.image.ImageUtils;
import com.training.socialnetwork.util.mapper.ObjectUtils;

@ExtendWith(MockitoExtension.class)
public class UserServiceTest {

	@InjectMocks
	private UserService userService;
	
	@Mock
	private ObjectUtils objectUtils;
	
	@Mock
	private ModelMapper modelMapper;
	
	@Mock
	private BCryptPasswordEncoder bCryptPasswordEncoder;
	
	@Mock
	private ImageUtils imageUtils;
	
	@Mock
	private UserRepository userRepository;
	
	@Mock
	private RoleRepository roleRepository;
	
	@Mock
	private FriendRepository friendRepository;
	
	@Mock
	private PostRepository postRepository;
	
	@Mock
	private LikeRepository likeRepository;
	
	@Mock
	private CommentRepository commentRepository;
	
	@Mock
	private PhotoRepository photoRepository;
	
	@Test
	public void createUserSuccess() throws Exception {
		UserRegisterRequestDto userRegisterDto = new UserRegisterRequestDto();
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
		
		UserRegisterResponseDto userRegistedDto = new UserRegisterResponseDto();
		
		when(userRepository.findByEmail(any())).thenReturn(null);
		when(userRepository.findByUsername(any())).thenReturn(null);
		when(roleRepository.findByName(any())).thenReturn(role);
		when(userRepository.save(any())).thenReturn(user);
		when(modelMapper.map(any(), any())).thenReturn(userRegistedDto);
		
		userRegistedDto = userService.createUser(userRegisterDto);
	}
	
	@Test
    public void createUserFalse() throws Exception {
		UserRegisterRequestDto userRegisterDto = new UserRegisterRequestDto();
		userRegisterDto.setUsername("test");
		userRegisterDto.setPassword("123456");
		userRegisterDto.setEmail("test@gmail.com");
		
		User user = new User();
		user.setUserId(1);
		user.setUsername("test");
		user.setPassword("123456");
		user.setEmail("test@gmail.com");
		user.setCreateDate(new Date());
		user.setUpdateDate(new Date());
		
        when(userRepository.findByEmail(anyString())).thenReturn(user);

        assertThrows(CustomException.class, () -> userService.createUser(userRegisterDto));
    }
	
	@Test
    public void createUserFalse2() throws Exception {
		UserRegisterRequestDto userRegisterDto = new UserRegisterRequestDto();
		userRegisterDto.setUsername("test");
		userRegisterDto.setPassword("123456");
		userRegisterDto.setEmail("test@gmail.com");
		
		User user = new User();
		user.setUserId(1);
		user.setUsername("test");
		user.setPassword("123456");
		user.setEmail("test@gmail.com");
		user.setCreateDate(new Date());
		user.setUpdateDate(new Date());
		
        when(userRepository.findByUsername(anyString())).thenReturn(user);

        assertThrows(CustomException.class, () -> userService.createUser(userRegisterDto));
    }
	
	@Test
	public void loginUserSuccess() throws Exception {
		String username = "test";
		String password = "123456";
		
		User user = new User();
		user.setUserId(1);
		user.setUsername("test");
		user.setPassword(bCryptPasswordEncoder.encode("123456"));
		user.setEmail("test@gmail.com");
		
		when(userRepository.findByUsername(username)).thenReturn(user);
		when(bCryptPasswordEncoder.matches(password, user.getPassword())).thenReturn(true);
		
		userService.loginUser(username, password);
	}
	
	@Test
    public void loginUserFail() throws Exception {
        String username = "test";
        String password = "654321";
        User user = new User();
        user.setUsername(username);
        user.setPassword(bCryptPasswordEncoder.encode("123456"));

        when(userRepository.findByUsername(username)).thenReturn(user);
        when(bCryptPasswordEncoder.matches(password, user.getPassword())).thenReturn(false);

        userService.loginUser(username, password);
    }
	
	@Test
    public void loginUserFail2() throws Exception {
        String username = "nonexistentUser";
        String password = "testPassword";

        when(userRepository.findByUsername(username)).thenReturn(null);

        assertThrows(CustomException.class, () -> userService.loginUser(username, password));
    }
	
	@Test
	public void updateInfoSuccess() throws Exception {
		UserUpdateRequestDto userUpdateDto = new UserUpdateRequestDto();
		userUpdateDto.setRealName("Test");
		userUpdateDto.setGender("female");
		userUpdateDto.setAddress("Hanoi");
		
		User userToUpdate = new User();
		userToUpdate.setUserId(1);
		userToUpdate.setUsername("test");
		userToUpdate.setEmail("test@gmail.com");
		userToUpdate.setBirthDate(new Date());
		
		UserUpdateResponseDto userUpdatedDto = new UserUpdateResponseDto();
		userUpdatedDto.setUserId(1);
		userUpdatedDto.setRealName("Test");
		userUpdatedDto.setSex("female");
		userUpdatedDto.setAddress("Hanoi");
		userUpdatedDto.setUsername("test");
		userUpdatedDto.setEmail("test@gmail.com");
		
		Photo avatar = new Photo();
		avatar.setPhotoId(1);
		avatar.setUser(userToUpdate);
		
		when(userRepository.findById(1)).thenReturn(Optional.of(userToUpdate));
		objectUtils.copyProperties(userUpdateDto, userToUpdate);
		when(photoRepository.findAvatarByUserId(anyInt())).thenReturn(avatar);
		when(userRepository.save(any())).thenReturn(userToUpdate);
		when(modelMapper.map(any(), any())).thenReturn(userUpdatedDto);
		
		userService.updateInfo(userUpdateDto, 1, 1);
	}
	
	@Test
    public void updateInfoUserFail() throws Exception {
        int userId = 1;
		UserUpdateRequestDto userUpdateDto = new UserUpdateRequestDto();
		userUpdateDto.setRealName("Test");
		userUpdateDto.setGender("female");
		userUpdateDto.setAddress("Hanoi");

        when(userRepository.findById(userId)).thenReturn(Optional.empty());

        assertThrows(CustomException.class, () -> userService.updateInfo(userUpdateDto, 1, 1));
    }
	
	@Test
    public void testUpdateInfoForbidden() throws Exception {
        int userId = 1;
        int loggedInUserId = 2;
		UserUpdateRequestDto userUpdateDto = new UserUpdateRequestDto();
		userUpdateDto.setRealName("Test");
		userUpdateDto.setGender("female");
		userUpdateDto.setAddress("Hanoi");
        User userToUpdate = new User();
        userToUpdate.setUserId(userId);
        User loggedInUser = new User();
        loggedInUser.setUserId(loggedInUserId);
        when(userRepository.findById(userId)).thenReturn(Optional.of(userToUpdate));
        when(userRepository.findById(loggedInUserId)).thenReturn(Optional.of(loggedInUser));

        assertThrows(CustomException.class, () -> userService.updateInfo(userUpdateDto, 1, 2));
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
		
		Photo avatar = new Photo();
		avatar.setPhotoId(1);
		avatar.setUser(user);
		
		UserDetailResponseDto userDetailDto = new UserDetailResponseDto();
		
		when(userRepository.findById(1)).thenReturn(Optional.of(user));
		when(photoRepository.findAvatarByUserId(anyInt())).thenReturn(avatar);
		when(modelMapper.map(any(), any())).thenReturn(userDetailDto);
		
		userService.getInfo(1);
	}
	
	@Test
	public void getInfoSuccess2() throws Exception {
		User user = new User();
		user.setUserId(1);
		user.setUsername("test");
		user.setPassword("123456");
		user.setEmail("test@gmail.com");
		user.setGender(1);
		user.setBirthDate(new Date());
		user.setAddress("Hanoi");
		
		Photo avatar = new Photo();
		avatar.setPhotoId(1);
		avatar.setUser(user);
		
		UserDetailResponseDto userDetailDto = new UserDetailResponseDto();
		
		when(userRepository.findById(1)).thenReturn(Optional.of(user));
		when(photoRepository.findAvatarByUserId(anyInt())).thenReturn(avatar);
		when(modelMapper.map(any(), any())).thenReturn(userDetailDto);
		
		userService.getInfo(1);
	}
	
	@Test
	public void getInfoFail() throws Exception {
		User user = new User();
		user.setUserId(1);
		user.setUsername("test");
		user.setPassword("123456");
		user.setEmail("test@gmail.com");
		user.setGender(1);
		user.setBirthDate(new Date());
		user.setAddress("Hanoi");
		
		when(userRepository.findById(1)).thenReturn(Optional.empty());
		
		assertThrows(CustomException.class, () -> userService.getInfo(1));
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
		
		Photo avatar = new Photo();
		avatar.setPhotoId(1);
		avatar.setUser(user2);
		
		Page<User> userPage = new PageImpl<User>(userList);
		
		Friend friend = new Friend();
		friend.setFriendId(1);
		friend.setSentUser(user1);
		friend.setReceivedUser(user3);
		friend.setStatus(1);
		
		List<Friend> friendList = new ArrayList<Friend>();
		friendList.add(friend);
		
		String keyword = "test";
		UserSearchResponseDto userSearchDto = new UserSearchResponseDto();
		when(userRepository.findAllUserByKeyword(anyInt(), any(), any())).thenReturn(userPage);
		when(friendRepository.findAllByUserId(anyInt())).thenReturn(friendList);
		when(photoRepository.findAvatarByUserId(anyInt())).thenReturn(avatar);
		when(modelMapper.map(any(), any())).thenReturn(userSearchDto);
		
		userService.searchUser(3, keyword, null);
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
	    public void getForgotPasswordFail() throws Exception {
	        String email = "test1@gmail.com";

	        when(userRepository.findByEmail(email)).thenReturn(null);

	        assertThrows(CustomException.class, () -> userService.forgotPassword(email));
	    }
	
	@Test
	public void getReportUserSuccess() {
		LocalDate date = LocalDate.now();
		TemporalField fieldISO = WeekFields.of(Locale.FRANCE).dayOfWeek();
		LocalDate dateStart = date.with(fieldISO, 1);
		LocalDate dateEnd = date.with(fieldISO, 7);
		
		UserReportResponseDto userReportDto = new UserReportResponseDto();
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
	
	@Test
    public void resetPasswordFail() {
        String token = "invalidToken";
        String newPassword = "newPassword";

        when(userRepository.findByToken(token)).thenReturn(null);

        assertThrows(CustomException.class, () -> userService.resetPassword(token, newPassword));
    }
	
}
