package com.training.socialnetwork.service.impl;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.ZoneId;
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
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.training.socialnetwork.dto.request.user.UserRegisterRequestDto;
import com.training.socialnetwork.dto.request.user.UserUpdateRequestDto;
import com.training.socialnetwork.dto.response.user.UserDetailResponseDto;
import com.training.socialnetwork.dto.response.user.UserForgotPasswordResponseDto;
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
import com.training.socialnetwork.service.IUserService;
import com.training.socialnetwork.util.constant.Constant;
import com.training.socialnetwork.util.exception.CustomException;
import com.training.socialnetwork.util.mapper.ObjectMapperUtils;

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
	private PhotoRepository photoRepository;

	@Autowired
	private ModelMapper modelMapper;

	@Autowired
	private BCryptPasswordEncoder bCryptPasswordEncoder;

	@Autowired
	private ObjectMapperUtils objectMapper;

	private static final long EXPIRE_TOKEN = 30;

	@Override
	public UserRegisterResponseDto createUser(UserRegisterRequestDto userRegisterDto) throws Exception {
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

		user = userRepository.save(user);

		UserRegisterResponseDto userRegistedDto = new UserRegisterResponseDto();
		userRegistedDto = modelMapper.map(user, UserRegisterResponseDto.class);
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
	public UserUpdateResponseDto updateInfo(UserUpdateRequestDto updateRequestDto, int userId, int loggedInUserId)
			throws Exception {
		User userToUpdate = userRepository.findById(userId).orElse(null);
		User loggedInUser = userRepository.findById(loggedInUserId).orElse(null);

		if (userToUpdate == null) {
			throw new CustomException(HttpStatus.NOT_FOUND, "User does not exist");
		}

		if (userToUpdate.getUserId() != loggedInUser.getUserId()) {
			throw new CustomException(HttpStatus.FORBIDDEN, "You do not have permission to update");
		}

		if (null != updateRequestDto.getGender()) {
			if (updateRequestDto.getGender().toUpperCase().equals(Constant.MALE)) {
				userToUpdate.setGender(Constant.NUMBER_0);
			} else {
				userToUpdate.setGender(Constant.NUMBER_1);
			}
		}

		if (null != updateRequestDto.getAvatar()) {
			Photo photo = photoRepository.findById(updateRequestDto.getAvatar()).orElse(null);

			if (photo == null) {
				throw new CustomException(HttpStatus.NOT_FOUND, "Photo does not exist");
			}
			if (photo.getUser().getUserId() != userId) {
				throw new CustomException(HttpStatus.FORBIDDEN, "You do not have permission to use this photo");
			}
			Photo avatar = photoRepository.findAvatarByUserId(loggedInUserId);
			if (avatar != null) {
				avatar.setAvatar(Constant.NUMBER_0);
				photoRepository.save(avatar);
			}

			photo.setAvatar(Constant.NUMBER_1);
			photoRepository.save(photo);
		}
		objectMapper.copyProperties(updateRequestDto, userToUpdate);
		if (null != updateRequestDto.getBirthDate()) {
			try {
				SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
				formatter.setLenient(false);
				userToUpdate.setBirthDate(formatter.parse(updateRequestDto.getBirthDate()));
			} catch (Exception e) {
				throw new CustomException(HttpStatus.BAD_REQUEST, "Invalid date");
			}
		}
		userToUpdate.setUserId(userId);
		userToUpdate.setUpdateDate(new Date());
		userToUpdate = userRepository.save(userToUpdate);

		UserUpdateResponseDto updateResponseDto = modelMapper.map(userToUpdate, UserUpdateResponseDto.class);
		if (null != userToUpdate.getGender()) {
			if (userToUpdate.getGender() == Constant.NUMBER_0) {
				updateResponseDto.setSex(Constant.MALE);
			} else {
				updateResponseDto.setSex(Constant.FEMALE);
			}
		}
		if (null != userToUpdate.getBirthDate()) {
			updateResponseDto
					.setBirthDate(userToUpdate.getBirthDate().toInstant().atZone(ZoneId.systemDefault()).toLocalDate());
		}

		if (null != updateRequestDto.getAvatar()) {
			updateResponseDto.setAvatar(updateRequestDto.getAvatar());
		} else {
			updateResponseDto.setAvatar(null);
		}
		return updateResponseDto;
	}

	@Override
	public UserDetailResponseDto getInfo(int userId) throws Exception {
		User user = userRepository.findById(userId).orElse(null);

		if (user == null) {
			throw new CustomException(HttpStatus.NOT_FOUND, "User does not exist");
		}

		UserDetailResponseDto userDetailDto = modelMapper.map(user, UserDetailResponseDto.class);
		if (null != user.getGender()) {
			if (user.getGender() == Constant.NUMBER_0) {
				userDetailDto.setGender(Constant.MALE);
			} else {
				userDetailDto.setGender(Constant.FEMALE);
			}
		}
		if (null != user.getBirthDate()) {
			userDetailDto.setBirthDate(user.getBirthDate().toInstant().atZone(ZoneId.systemDefault()).toLocalDate());
		}

		return userDetailDto;
	}

	@Override
	public Page<UserSearchResponseDto> searchUser(int userId, String keyword, Pageable paging) {
		Page<User> userList = userRepository.findAllUserByKeyword(userId, keyword, paging);

		List<Friend> friendList = friendRepository.findAllByUserId(userId);

		List<UserSearchResponseDto> userSearchList = new ArrayList<>();
		for (User user : userList) {
			UserSearchResponseDto userSearchDto = modelMapper.map(user, UserSearchResponseDto.class);
			userSearchList.add(userSearchDto);
		}
		for (UserSearchResponseDto user : userSearchList) {
			user.setFriendStatus(Constant.SEND_REQUEST);
			for (Friend friend : friendList) {
				if (friend.getSentUser().getUserId() == user.getUserId()
						|| friend.getReceivedUser().getUserId() == user.getUserId()) {
					if (friend.getStatus() == Constant.NUMBER_0) {
						user.setFriendStatus(Constant.SENT_REQUEST);
					} else if (friend.getStatus() == Constant.NUMBER_1) {
						user.setFriendStatus(Constant.FRIENDED);
					} else {
						user.setFriendStatus(Constant.SEND_REQUEST);
					}
					break;
				}
			}
		}

		Page<UserSearchResponseDto> result = new PageImpl<UserSearchResponseDto>(userSearchList);

		return result;
	}

	@Override
	public UserReportResponseDto getReportUser(int userId) {
		LocalDate date = LocalDate.now();
		TemporalField fieldISO = WeekFields.of(Locale.FRANCE).dayOfWeek();
		LocalDate dateStart = date.with(fieldISO, 1);
		LocalDate dateEnd = date.with(fieldISO, 7);

		UserReportResponseDto userReportDto = new UserReportResponseDto();
		userReportDto.setPostCount(postRepository.countPost(userId, dateStart, dateEnd));
		userReportDto.setCommentCount(commentRepository.countComment(userId, dateStart, dateEnd));
		userReportDto.setFriendCount(friendRepository.countFriend(userId, dateStart, dateEnd));
		userReportDto.setLikeCount(likeRepository.countLike(userId, dateStart, dateEnd));

		return userReportDto;
	}

	@Override
	public UserForgotPasswordResponseDto forgotPassword(String email) throws Exception {
		User user = userRepository.findByEmail(email);

		if (user == null) {
			throw new CustomException(HttpStatus.NOT_FOUND, "User does not exist");
		}
		String token = UUID.randomUUID().toString();
		user.setToken(token);
		user.setTokenCreateDate(new Date());

		user = userRepository.save(user);

		UserForgotPasswordResponseDto userForgotPasswordDto = new UserForgotPasswordResponseDto();
		userForgotPasswordDto.setToken(user.getToken());

		return userForgotPasswordDto;
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
