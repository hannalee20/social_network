package com.training.socialnetwork.controller;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.training.socialnetwork.dto.request.user.UserForgotPasswordRequestDto;
import com.training.socialnetwork.dto.request.user.UserGetTokenRequestDto;
import com.training.socialnetwork.dto.request.user.UserLoginRequestDto;
import com.training.socialnetwork.dto.request.user.UserRegisterRequestDto;
import com.training.socialnetwork.dto.request.user.UserResetPasswordRequestDto;
import com.training.socialnetwork.dto.request.user.UserUpdateRequestDto;
import com.training.socialnetwork.dto.response.common.MessageResponseDto;
import com.training.socialnetwork.dto.response.user.UserDetailResponseDto;
import com.training.socialnetwork.dto.response.user.UserForgotPasswordResponseDto;
import com.training.socialnetwork.dto.response.user.UserGetTokenResponseDto;
import com.training.socialnetwork.dto.response.user.UserLoginResponseDto;
import com.training.socialnetwork.dto.response.user.UserRegisterResponseDto;
import com.training.socialnetwork.dto.response.user.UserReportResponseDto;
import com.training.socialnetwork.dto.response.user.UserSearchResponseDto;
import com.training.socialnetwork.dto.response.user.UserUpdateResponseDto;
import com.training.socialnetwork.security.JwtUtils;
import com.training.socialnetwork.security.OtpUtils;
import com.training.socialnetwork.service.IUserService;
import com.training.socialnetwork.util.constant.Constant;
import com.training.socialnetwork.util.exception.CustomException;
import com.training.socialnetwork.util.generator.ReportGenerator;

@RestController
@RequestMapping(value = "/user")
public class UserController {

	@Autowired
	private IUserService userService;

	@Autowired
	private AuthenticationManager authenticationManager;

	@Autowired
	private JwtUtils jwtUtils;

	@Autowired
	private OtpUtils otpUtils;

	@PostMapping(value = "/register")
	public ResponseEntity<Object> registerUser(@RequestBody UserRegisterRequestDto userRegisterDto) {
		try {
			UserRegisterResponseDto result = userService.createUser(userRegisterDto);

			return new ResponseEntity<Object>(result, HttpStatus.CREATED);
		} catch (CustomException e) {
			MessageResponseDto result = new MessageResponseDto();
			result.setMessage(e.getMessage());

			return new ResponseEntity<Object>(result, e.getHttpStatus());
		} catch (Exception e) {
			MessageResponseDto result = new MessageResponseDto();
			result.setMessage(e.getMessage());

			return new ResponseEntity<Object>(result, HttpStatus.INTERNAL_SERVER_ERROR);
		}

	}

	@PostMapping(value = "/login")
	public ResponseEntity<Object> loginUser(@RequestBody UserLoginRequestDto userLoginDto) throws Exception {
		try {
			boolean checkLogin = userService.loginUser(userLoginDto.getUsername(), userLoginDto.getPassword());

			if (checkLogin) {
				int otp = otpUtils.generateOtp(userLoginDto.getUsername());
				return ResponseEntity.ok(new UserLoginResponseDto(String.valueOf(otp)));
			} else {
				MessageResponseDto result = new MessageResponseDto();
				result.setMessage(Constant.INVALID_USERNAME_OR_PASSWORD);
				return new ResponseEntity<Object>(result, HttpStatus.BAD_REQUEST);
			}
		} catch (CustomException e) {
			MessageResponseDto result = new MessageResponseDto();
			result.setMessage(e.getMessage());

			return new ResponseEntity<Object>(result, e.getHttpStatus());
		} catch (Exception e) {
			MessageResponseDto result = new MessageResponseDto();
			result.setMessage(e.getMessage());

			return new ResponseEntity<Object>(result, HttpStatus.INTERNAL_SERVER_ERROR);
		}

	}

	@PostMapping(value = "/token")
	public ResponseEntity<Object> getToken(@RequestBody UserGetTokenRequestDto userTokenDto) {
		Authentication authentication = authenticationManager.authenticate(
				new UsernamePasswordAuthenticationToken(userTokenDto.getUsername(), userTokenDto.getPassword()));
		SecurityContextHolder.getContext().setAuthentication(authentication);

		int otpFromCache = 0;

		if (userTokenDto.getOtp() >= 0) {
			otpFromCache = otpUtils.getOtp(userTokenDto.getUsername());
		}

		if (otpFromCache > 0 && otpFromCache == userTokenDto.getOtp() && authentication != null) {
			otpUtils.clearOtp(userTokenDto.getUsername());
			String jwt = jwtUtils.generateToken(authentication);
			return ResponseEntity.ok(new UserGetTokenResponseDto(jwt));
		} else {
			return new ResponseEntity<Object>(Constant.INVALID, HttpStatus.BAD_REQUEST);
		}
	}

	@PatchMapping(value = "/update/{userId}")
	public ResponseEntity<Object> updateUser(HttpServletRequest request, @PathVariable(value = "userId") int userId,
			@RequestBody(required = false) @Valid UserUpdateRequestDto userUpdateDto) {
		int loggedInUserId = jwtUtils.getUserIdFromJwt(jwtUtils.getJwt(request));
		try {
			UserUpdateResponseDto result = userService.updateInfo(userUpdateDto, userId, loggedInUserId);

			return new ResponseEntity<Object>(result, HttpStatus.OK);
		} catch (CustomException e) {
			MessageResponseDto result = new MessageResponseDto();
			result.setMessage(e.getMessage());

			return new ResponseEntity<Object>(result, e.getHttpStatus());
		} catch (Exception e) {
			MessageResponseDto result = new MessageResponseDto();
			result.setMessage(e.getMessage());

			return new ResponseEntity<Object>(result, HttpStatus.INTERNAL_SERVER_ERROR);
		}

	}

	@GetMapping(value = "/detail/{userId}")
	public ResponseEntity<Object> getOtherUserInfo(@PathVariable(value = "userId") int userId) {

		try {
			UserDetailResponseDto result = userService.getInfo(userId);

			return new ResponseEntity<>(result, HttpStatus.OK);
		} catch (CustomException e) {
			MessageResponseDto result = new MessageResponseDto();
			result.setMessage(e.getMessage());

			return new ResponseEntity<Object>(result, e.getHttpStatus());
		} catch (Exception e) {
			MessageResponseDto result = new MessageResponseDto();
			result.setMessage(e.getMessage());

			return new ResponseEntity<Object>(result, HttpStatus.INTERNAL_SERVER_ERROR);
		}

	}

	@GetMapping(value = "/detail")
	public ResponseEntity<Object> getUserInfo(HttpServletRequest request) {

		try {
			int loggedInUserId = jwtUtils.getUserIdFromJwt(jwtUtils.getJwt(request));
			UserDetailResponseDto result = userService.getInfo(loggedInUserId);

			return new ResponseEntity<>(result, HttpStatus.OK);
		} catch (CustomException e) {
			MessageResponseDto result = new MessageResponseDto();
			result.setMessage(e.getMessage());

			return new ResponseEntity<Object>(result, e.getHttpStatus());
		} catch (Exception e) {
			MessageResponseDto result = new MessageResponseDto();
			result.setMessage(e.getMessage());

			return new ResponseEntity<Object>(result, HttpStatus.INTERNAL_SERVER_ERROR);
		}

	}

	@GetMapping(value = "/search")
	public ResponseEntity<Object> searchUser(HttpServletRequest request,
			@RequestParam(value = "keyword") String keyword,
			@RequestParam(defaultValue = Constant.STRING_0, required = false) int page,
			@RequestParam(defaultValue = Constant.STRING_5, required = false) int pageSize) {
		int userId = jwtUtils.getUserIdFromJwt(jwtUtils.getJwt(request));
		Pageable paging = PageRequest.of(page, pageSize);

		try {
			Page<UserSearchResponseDto> userSearchList = userService.searchUser(userId, keyword, paging);

			Map<String, Object> result = new HashMap<>();
			result.put("userList", userSearchList.getContent());
			result.put("currentPage", userSearchList.getNumber());
			result.put("totalItems", userSearchList.getTotalElements());
			result.put("totalPages", userSearchList.getTotalPages());

			return new ResponseEntity<Object>(result, HttpStatus.OK);
		} catch (CustomException e) {
			MessageResponseDto result = new MessageResponseDto();
			result.setMessage(e.getMessage());

			return new ResponseEntity<Object>(result, e.getHttpStatus());
		} catch (Exception e) {
			MessageResponseDto result = new MessageResponseDto();
			result.setMessage(e.getMessage());

			return new ResponseEntity<Object>(result, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@GetMapping(value = "/export-report")
	public ResponseEntity<Object> getReportUser(HttpServletRequest request, HttpServletResponse response) {
		response.setContentType("application/octet-stream");
		DateFormat dateFormatter = new SimpleDateFormat("yyyy-MM-dd");
		String currentDate = dateFormatter.format(new Date());

		String headerKey = "Content-Disposition";
		String headerValue = "attachment; filename=report_" + currentDate + ".xlsx";
		response.setHeader(headerKey, headerValue);

		int userId = jwtUtils.getUserIdFromJwt(jwtUtils.getJwt(request));
		MessageResponseDto result = new MessageResponseDto();

		try {
			UserReportResponseDto user = userService.getReportUser(userId);

			ReportGenerator reportGenerator = new ReportGenerator(user);
			reportGenerator.export(response);

			result.setMessage(Constant.EXPORT_REPORT_SUCCESSFULLY);
			return new ResponseEntity<>(result, HttpStatus.OK);
		} catch (CustomException e) {
			result.setMessage(e.getMessage());

			return new ResponseEntity<Object>(result, e.getHttpStatus());
		} catch (Exception e) {
			result.setMessage(e.getMessage());

			return new ResponseEntity<Object>(result, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@PostMapping(value = "/forgot-password")
	public ResponseEntity<Object> forgotPassword(@RequestBody UserForgotPasswordRequestDto userForgotPasswordDto) {
		try {
			UserForgotPasswordResponseDto result = userService.forgotPassword(userForgotPasswordDto.getEmail());

			return new ResponseEntity<Object>(result, HttpStatus.OK);
		} catch (CustomException e) {
			MessageResponseDto result = new MessageResponseDto();
			result.setMessage(e.getMessage());

			return new ResponseEntity<Object>(result, e.getHttpStatus());
		} catch (Exception e) {
			MessageResponseDto result = new MessageResponseDto();
			result.setMessage(e.getMessage());

			return new ResponseEntity<Object>(result, HttpStatus.INTERNAL_SERVER_ERROR);
		}

	}

	@PutMapping(value = "/reset-password")
	public ResponseEntity<Object> resetPassword(@RequestBody UserResetPasswordRequestDto userResetPasswordDto) {
		try {
			String result = userService.resetPassword(userResetPasswordDto.getToken(),
					userResetPasswordDto.getNewPassword());

			return new ResponseEntity<Object>(result, HttpStatus.OK);
		} catch (CustomException e) {
			MessageResponseDto result = new MessageResponseDto();
			result.setMessage(e.getMessage());

			return new ResponseEntity<Object>(result, e.getHttpStatus());
		} catch (Exception e) {
			MessageResponseDto result = new MessageResponseDto();
			result.setMessage(e.getMessage());

			return new ResponseEntity<Object>(result, HttpStatus.INTERNAL_SERVER_ERROR);
		}

	}

}