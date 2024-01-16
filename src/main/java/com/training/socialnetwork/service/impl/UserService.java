package com.training.socialnetwork.service.impl;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.time.temporal.TemporalField;
import java.time.temporal.WeekFields;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.UUID;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
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
import com.training.socialnetwork.util.constant.Constant;

@Service
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

	private static final long EXPIRE_TOKEN = 30;

	@Override
	public UserRegistedDto createUser(UserRegisterDto userRegisterDto) throws Exception {
		if (userRepository.findByUsername(userRegisterDto.getUsername()) != null) {
			throw new Exception(Constant.SERVER_ERROR);
		}
		User user = new User();
		user.setUsername(userRegisterDto.getUsername());
		user.setPassword(bCryptPasswordEncoder.encode(userRegisterDto.getPassword()));
		user.setEmail(userRegisterDto.getEmail());
		Role role = roleRepository.findByName("ROLE_USER");
//		Set<Role> roles = new HashSet<>();
//		roles.add(role);
		user.setRole(role);
		user.setCreateDate(new Date());
		user.setUpdateDate(new Date());

		User userRegisted = userRepository.save(user);

		if (userRegisted != null) {
			return modelMapper.map(userRegisted, UserRegistedDto.class);
		}

		throw new Exception(Constant.SERVER_ERROR);
	}

	@Override
	public boolean loginUser(String username, String password) throws Exception {
		User user = userRepository.findByUsername(username);
		if (user != null && bCryptPasswordEncoder.matches(password, user.getPassword())) {
			return true;
		}

		throw new Exception(Constant.INVALID_USERNAME_OR_PASSWORD);
	}

	@Override
	public UserUpdatedDto updateInfo(UserUpdateDto userUpdateDto, MultipartFile avatar, int userId, int loggedInUserId)
			throws Exception {
		User userToUpdate = userRepository.findById(userId).orElse(null);
		User loggedInUser = userRepository.findById(loggedInUserId).orElse(null);

		if (userToUpdate == null || loggedInUser == null || userToUpdate.getUserId() != loggedInUser.getUserId()) {
			throw new Exception(Constant.SERVER_ERROR);
		}

		User user = modelMapper.map(userUpdateDto, User.class);

		user.setUsername(userToUpdate.getUsername());
		user.setPassword(userToUpdate.getPassword());
		user.setRole(userToUpdate.getRole());
		if (avatar != null) {
			byte[] bytes = avatar.getBytes();
			Path pathAvatar = Paths.get(".").resolve("profile_avatar");
			Files.delete(pathAvatar.resolve(user.getAvatarUrl()));
			String fileName = avatar.getOriginalFilename();
			String extension = getFileExtension(fileName);
			fileName = user.getUserId() + "." + extension;
			Files.write(pathAvatar.resolve(fileName), bytes);
			user.setAvatarUrl(fileName);
		}
		User userUpdated = userRepository.save(user);

		if (userUpdated != null) {
			return modelMapper.map(userUpdated, UserUpdatedDto.class);
		}

		throw new Exception(Constant.SERVER_ERROR);
	}
	
	private static String getFileExtension(String fileName) {
        int index = fileName.lastIndexOf('.');
        return index == -1 ? fileName : fileName.substring(index + 1);
    }

	@Override
	public UserDetailDto getInfo(int userId) throws Exception {
		User user = userRepository.findById(userId).orElse(null);

		if (user == null) {
			throw new Exception(Constant.SERVER_ERROR);
		}

		return modelMapper.map(user, UserDetailDto.class);
	}

	@Override
	public List<UserSearchDto> searchUser(int userId, String keyword) {
		List<User> userList = userRepository.findAllUserByKeyword(userId, keyword);

		List<Friend> friendList = friendRepository.findAllByUserId(userId);

		List<UserSearchDto> userSearchList = userList.stream().map(user -> modelMapper.map(user, UserSearchDto.class))
				.collect(Collectors.toList());

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
	public String forgotPassword(String email, int userId) throws Exception {
		User user = userRepository.findByEmail(email);

		if (user == null || user.getUserId() != userId) {
			throw new Exception(Constant.SERVER_ERROR);
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
		if(user == null) {
			throw new Exception(Constant.SERVER_ERROR);
		}
		
		if(user.getTokenCreateDate().compareTo(new Date()) > EXPIRE_TOKEN) {
			throw new Exception(Constant.SERVER_ERROR);
		}
		user.setPassword(newPassword);
		user.setToken(null);
		user.setTokenCreateDate(null);
		userRepository.save(user);
		return "success";
	}

}
