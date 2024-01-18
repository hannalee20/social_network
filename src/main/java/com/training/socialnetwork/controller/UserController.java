package com.training.socialnetwork.controller;

import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.training.socialnetwork.dto.request.user.UserRegisterDto;
import com.training.socialnetwork.dto.request.user.UserUpdateDto;
import com.training.socialnetwork.dto.response.user.JwtResponse;
import com.training.socialnetwork.dto.response.user.OtpResponse;
import com.training.socialnetwork.dto.response.user.UserDetailDto;
import com.training.socialnetwork.dto.response.user.UserRegistedDto;
import com.training.socialnetwork.dto.response.user.UserReportDto;
import com.training.socialnetwork.dto.response.user.UserSearchDto;
import com.training.socialnetwork.dto.response.user.UserUpdatedDto;
import com.training.socialnetwork.security.JwtUtils;
import com.training.socialnetwork.security.OtpUtils;
import com.training.socialnetwork.service.IUserService;
import com.training.socialnetwork.util.constant.Constant;
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
	public ResponseEntity<Object> registerUser(@RequestBody UserRegisterDto userRegisterDto) {
		try {
			UserRegistedDto result = userService.createUser(userRegisterDto);
			
			return new ResponseEntity<Object>(result, HttpStatus.CREATED);
		} catch (Exception e) {
			return new ResponseEntity<Object>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
		}

	}

	@PostMapping(value = "/login")
	public ResponseEntity<Object> loginUser(@RequestParam("username") String username,
			@RequestParam("password") String password) throws Exception {
		try {
			boolean checkLogin = userService.loginUser(username, password);

			if (checkLogin) {
				int otp = otpUtils.generateOtp(username);
				return ResponseEntity.ok(new OtpResponse(String.valueOf(otp)));
			} else {
				return ResponseEntity.ok(Constant.INVALID_USERNAME_OR_PASSWORD);
			}
		} catch (Exception e) {
			return new ResponseEntity<Object>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
		}

	}

	@PostMapping(value = "/token")
	public ResponseEntity<Object> getToken(@RequestParam("username") String username,
			@RequestParam("password") String password, @RequestParam("otp") int otp) {
		Authentication authentication = authenticationManager
				.authenticate(new UsernamePasswordAuthenticationToken(username, password));
		SecurityContextHolder.getContext().setAuthentication(authentication);

		int otpFromCache = 0;

		if (otp >= 0) {
			otpFromCache = otpUtils.getOtp(username);
		}

		if (otpFromCache > 0 && otpFromCache == otp && authentication != null) {
			otpUtils.clearOtp(username);
			String jwt = jwtUtils.generateToken(authentication);
			return ResponseEntity.ok(new JwtResponse(jwt));
		}

		return ResponseEntity.ok(Constant.SERVER_ERROR);
	}

	@PutMapping(value = "/update/{userId}", consumes = {"multipart/form-data"})
	public ResponseEntity<Object> updateUser(HttpServletRequest request, @RequestParam UserUpdateDto userUpdateDto,
			@RequestParam(required = false) MultipartFile avatar, @PathVariable(value = "userId") int userId) {
		int loggedInUserId = jwtUtils.getUserIdFromJwt(jwtUtils.getJwt(request));
		try {
			UserUpdatedDto result = userService.updateInfo(userUpdateDto, avatar, userId, loggedInUserId);

			return new ResponseEntity<Object>(result, HttpStatus.OK);
		} catch (Exception e) {
			return new ResponseEntity<Object>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
		}

	}

	@GetMapping(value = "/detail/{userId}")
	public ResponseEntity<Object> getUserInfo(@PathVariable(value = "userId") int userId) {

		try {
			UserDetailDto result = userService.getInfo(userId);

			return new ResponseEntity<>(result, HttpStatus.OK);
		} catch (Exception e) {
			return new ResponseEntity<Object>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
		}

	}

	@PostMapping(value = "/search")
	public ResponseEntity<Object> searchUser(HttpServletRequest request, @RequestParam("keyword") String keyword) {
		int userId = jwtUtils.getUserIdFromJwt(jwtUtils.getJwt(request));

		try {
			List<UserSearchDto> userSearchList = userService.searchUser(userId, keyword);
			return new ResponseEntity<Object>(userSearchList, HttpStatus.OK);
		} catch (Exception e) {
			return new ResponseEntity<Object>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
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

		try {
			UserReportDto user = userService.getReportUser(userId);

			ReportGenerator reportGenerator = new ReportGenerator(user);
			reportGenerator.export(response);

			return new ResponseEntity<>(Constant.EXPORT_REPORT_SUCCESSFULLY, HttpStatus.OK);
		} catch (IOException e) {
			return new ResponseEntity<Object>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@PostMapping(value = "/forgot-password")
	public ResponseEntity<Object> forgotPassword(@RequestParam("email") String email) {
		try {
			String result = userService.forgotPassword(email);
			if (result != null) {
				result = "http://localhost:8080/user/reset-password?token=\\" + result;
			}
			
			return ResponseEntity.ok(result);
		} catch (Exception e) {
			return new ResponseEntity<Object>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
		}

	}

	@PutMapping(value = "/reset-password")
	public ResponseEntity<Object> resetPassword(@RequestParam("token") String token,
			@RequestParam("newPassword") String newPassword) {
		try {
			String result = userService.resetPassword(token, newPassword);
			
			return ResponseEntity.ok(result);
		} catch (Exception e) {
			return new ResponseEntity<Object>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
		}

	}
}
