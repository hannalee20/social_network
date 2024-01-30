package com.training.socialnetwork.service.impl;

import java.time.LocalDate;
import java.time.temporal.TemporalField;
import java.time.temporal.WeekFields;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.UUID;

import javax.transaction.Transactional;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.training.socialnetwork.dto.request.user.UserRegisterDto;
import com.training.socialnetwork.dto.request.user.UserUpdateDto;
import com.training.socialnetwork.dto.response.user.UserDetailDto;
import com.training.socialnetwork.dto.response.user.UserRegistedDto;
import com.training.socialnetwork.dto.response.user.UserReportDto;
import com.training.socialnetwork.dto.response.user.UserSearchDto;
import com.training.socialnetwork.dto.response.user.UserUpdatedDto;
import com.training.socialnetwork.entity.Friend;
import com.training.socialnetwork.entity.Role;
import com.training.socialnetwork.entity.User;
import com.training.socialnetwork.repository.CommentRepository;
import com.training.socialnetwork.repository.FriendRepository;
import com.training.socialnetwork.repository.LikeRepository;
import com.training.socialnetwork.repository.PostRepository;
import com.training.socialnetwork.repository.RoleRepository;
import com.training.socialnetwork.repository.UserRepository;
import com.training.socialnetwork.service.IUserService;
import com.training.socialnetwork.util.CustomException;
import com.training.socialnetwork.util.constant.Constant;
import com.training.socialnetwork.util.image.ImageUtils;
import com.training.socialnetwork.util.mapper.ObjectMapper;

@Service
@Transactional
public class UserService implements IUserService {

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private PostRepository postRepository;

	@Autowired
	private CommentRepository commentRepository;

	@Autowired
	private LikeRepository likeRepository;

	@Autowired
	private FriendRepository friendRepository;

	@Autowired
	private RoleRepository roleRepository;

	@Autowired
	private ModelMapper modelMapper;

	@Autowired
	private BCryptPasswordEncoder bCryptPasswordEncoder;

	@Autowired
	private ImageUtils imageUtils;

	@Autowired
	private ObjectMapper objectMapper;

	private static final long EXPIRE_TOKEN = 30;

	@Override
	public UserRegistedDto createUser(UserRegisterDto userRegisterDto) throws Exception {
		if (userRepository.findByEmail(userRegisterDto.getEmail()) != null) {
			throw new CustomException(HttpStatus.BAD_REQUEST, "Email already exists");
		}

		if (userRepository.findByUsername(userRegisterDto.getUsername()) != null) {
			throw new CustomException(HttpStatus.BAD_REQUEST, "Username already exists");
		}
		User user = new User();
		user.setUsername(userRegisterDto.getUsername());
		user.setPassword(bCryptPasswordEncoder.encode(userRegisterDto.getPassword()));
		user.setEmail(userRegisterDto.getEmail());
		Role role = roleRepository.findByName(Constant.ROLE_USER);
		user.setRole(role);
		user.setCreateDate(new Date());
		user.setUpdateDate(new Date());

		User userRegisted = userRepository.save(user);

		UserRegistedDto userRegistedDto = new UserRegistedDto();
		userRegistedDto = modelMapper.map(userRegisted, UserRegistedDto.class);
		userRegistedDto.setRole(Constant.ROLE_USER);
		return userRegistedDto;
	}

	@Override
	public boolean loginUser(String username, String password) throws Exception {
		User user = userRepository.findByUsername(username);
		
		if (user == null) {
			throw new CustomException(HttpStatus.NOT_FOUND, "User does not exist");
		}
		
		if (bCryptPasswordEncoder.matches(password, user.getPassword())) {
			return true;
		}

		return false;
	}

	@Override
	public UserUpdatedDto updateInfo(UserUpdateDto userUpdateDto, MultipartFile avatar, int userId, int loggedInUserId)
			throws Exception {
		User userToUpdate = userRepository.findById(userId).orElse(null);
		User loggedInUser = userRepository.findById(loggedInUserId).orElse(null);

		if (userToUpdate == null) {
			throw new CustomException(HttpStatus.NOT_FOUND, "User does not exist");
		}

		if (userToUpdate.getUserId() != loggedInUser.getUserId()) {
			throw new CustomException(HttpStatus.FORBIDDEN, "You do not have permission to update");
		}

		if (userUpdateDto.getSex() != null) {
			if (userUpdateDto.getSex().toUpperCase().equals(Constant.MALE)) {
				userToUpdate.setGender(Constant.NUMBER_0);
			} else {
				userToUpdate.setGender(Constant.NUMBER_1);
			}
		}
		objectMapper.copyProperties(userUpdateDto, userToUpdate);
		userToUpdate.setUserId(userId);
		if (avatar != null) {
			String avatarUrl = imageUtils.saveImage(avatar);
			userToUpdate.setAvatarUrl(avatarUrl);
		}
		userToUpdate.setUpdateDate(new Date());
		User userUpdated = userRepository.save(userToUpdate);

		return modelMapper.map(userUpdated, UserUpdatedDto.class);
	}

	@Override
	public UserDetailDto getInfo(int userId) throws Exception {
		User user = userRepository.findById(userId).orElse(null);

		if (user == null) {
			throw new CustomException(HttpStatus.NOT_FOUND, "User does not exist");
		}

		UserDetailDto userDetailDto = modelMapper.map(user, UserDetailDto.class);
		if (user.getGender() == Constant.NUMBER_0) {
			userDetailDto.setGender(Constant.MALE);
		} else {
			userDetailDto.setGender(Constant.FEMALE);
		}

		return userDetailDto;
	}

	@Override
	public List<UserSearchDto> searchUser(int userId, String keyword, Pageable paging) {
		List<User> userList = userRepository.findAllUserByKeyword(userId, keyword);

		List<Friend> friendList = friendRepository.findAllByUserId(userId);

		List<UserSearchDto> userSearchList = new ArrayList<>();
		for (User user : userList) {
			UserSearchDto userSearchDto = modelMapper.map(user, UserSearchDto.class);
			userSearchList.add(userSearchDto);
		}
		for (UserSearchDto user : userSearchList) {
			user.setFriendStatus(Constant.NOT_FRIEND);
			for (Friend friend : friendList) {
				if (friend.getUser1().getUserId() == user.getUserId()
						|| friend.getUser2().getUserId() == user.getUserId()) {
					user.setFriendStatus(friend.getStatus());
					break;
				}
			}
		}

		return userSearchList;
	}

	@Override
	public UserReportDto getReportUser(int userId) {
		LocalDate date = LocalDate.now();
		TemporalField fieldISO = WeekFields.of(Locale.FRANCE).dayOfWeek();
		LocalDate dateStart = date.with(fieldISO, 1);
		LocalDate dateEnd = date.with(fieldISO, 7);

		UserReportDto userReportDto = new UserReportDto();
		userReportDto.setPostCount(postRepository.countPost(userId, dateStart, dateEnd));
		userReportDto.setCommentCount(commentRepository.countComment(userId, dateStart, dateEnd));
		userReportDto.setFriendCount(friendRepository.countFriend(userId, dateStart, dateEnd));
		userReportDto.setLikeCount(likeRepository.countLike(userId, dateStart, dateEnd));

		return userReportDto;
	}

	@Override
	public String forgotPassword(String email) throws Exception {
		User user = userRepository.findByEmail(email);

		if (user == null) {
			throw new CustomException(HttpStatus.NOT_FOUND, "User does not exist");
		}
		String token = UUID.randomUUID().toString();
		user.setToken(token);
		user.setTokenCreateDate(new Date());

		user = userRepository.save(user);

		return user.getToken();
	}

	@Override
	public String resetPassword(String token, String newPassword) throws Exception {
		User user = userRepository.findByToken(token);
		if (user == null) {
			throw new CustomException(HttpStatus.BAD_REQUEST, Constant.TOKEN_INVALID);
		}

		if (user.getTokenCreateDate().compareTo(new Date()) > EXPIRE_TOKEN) {
			throw new CustomException(HttpStatus.BAD_REQUEST, Constant.TOKEN_HAS_EXPIRED);
		}
		user.setPassword(bCryptPasswordEncoder.encode(newPassword));
		user.setToken(null);
		user.setTokenCreateDate(null);
		user.setUpdateDate(new Date());
		userRepository.save(user);
		return Constant.RESET_PASSWORD_SUCCESSFULLY;
	}

}
