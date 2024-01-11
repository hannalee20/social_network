package com.training.socialnetwork.controller;

import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
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
import org.springframework.web.bind.annotation.RestController;

import com.training.socialnetwork.dto.request.user.UserRegisterDto;
import com.training.socialnetwork.dto.request.user.UserUpdateDto;
import com.training.socialnetwork.dto.response.user.JwtResponse;
import com.training.socialnetwork.dto.response.user.UserDetailDto;
import com.training.socialnetwork.dto.response.user.UserRegistedDto;
import com.training.socialnetwork.dto.response.user.UserReportDto;
import com.training.socialnetwork.dto.response.user.UserSearchDto;
import com.training.socialnetwork.dto.response.user.UserUpdatedDto;
import com.training.socialnetwork.security.JwtUtils;
import com.training.socialnetwork.service.IUserService;
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
	
	@PostMapping(value = "/register")
	public ResponseEntity<Object> registerUser(@RequestBody UserRegisterDto userRegisterDto) throws Exception {
		UserRegistedDto result = userService.createUser(userRegisterDto);

		return new ResponseEntity<Object>(result, HttpStatus.CREATED);
	}

	@PostMapping(value = "/login")
	public ResponseEntity<Object> loginUser(@RequestParam("username") String username,
			@RequestParam("password") String password) throws Exception {
//		Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(username, password));
//		
//		SecurityContextHolder.getContext().setAuthentication(authentication);
//		CustomUserDetail customUserDetail = (CustomUserDetail) authentication.getPrincipal();
//		ResponseCookie jwtCookie = jwtUtils.generateJwtCookie(customUserDetail);
//		List<String> roles = customUserDetail.getAuthorities().stream()
//				.map(item -> item.getAuthority())
//				.collect(Collectors.toList());
		
//		UserLoggedInDto result = userService.loginUser(username, password);
//		HttpHeaders responseHeaders = new HttpHeaders();
//		responseHeaders.set("userId", Integer.toString(customUserDetail.getUserId()));
//		responseHeaders.set("token", jwtCookie.toString());
//		
//		return new ResponseEntity<Object>(jwtCookie.toString(), responseHeaders, HttpStatus.OK);
//		
//		return ResponseEntity.ok().header(HttpHeaders.SET_COOKIE, jwtCookie.toString())
//				.body(new UserLoggedInDto(customUserDetail.getUserId(),
//											customUserDetail.getUsername(),
//											roles));
		Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(username, password));
		SecurityContextHolder.getContext().setAuthentication(authentication);
		
		if(authentication != null) {
			String jwt= jwtUtils.generateToken(authentication);
			return ResponseEntity.ok(new JwtResponse(jwt));
		}
		return ResponseEntity.ok("fail");
	}

	@PutMapping(value = "/update/{userId}")
	public ResponseEntity<Object> updateUser(@RequestBody @Valid UserUpdateDto userUpdateDto,
			@PathVariable(value = "userId") int userId) throws Exception {
		UserUpdatedDto result = userService.updateInfo(userUpdateDto, userId);

		return new ResponseEntity<Object>(result, HttpStatus.OK);
	}

	@GetMapping(value = "/detail/{userId}")
	public ResponseEntity<Object> getUserInfo(@PathVariable(value = "userId") int userId) throws Exception {
		UserDetailDto result = userService.getInfo(userId);

		return new ResponseEntity<>(result, HttpStatus.OK);
	}
	
	@PostMapping(value = "/search")
	public List<UserSearchDto> searchUser(HttpServletRequest request, @RequestParam("keyword") String keyword){
		int userId = jwtUtils.getUserIdFromJwt(jwtUtils.getJwt(request));
		
		return userService.searchUser(userId, keyword);
	}
	
	@GetMapping(value = "/export-report")
	public void getReportUser(HttpServletRequest request, HttpServletResponse response) throws IOException {
		response.setContentType("application/octet-stream");
		DateFormat dateFormatter = new SimpleDateFormat("yyyy-MM-dd");
		String currentDate = dateFormatter.format(new Date());
		
		String headerKey = "Content-Disposition";
		String headerValue = "attachment; filename=report_" + currentDate + ".xlsx";
		response.setHeader(headerKey, headerValue);
		
		int userId = jwtUtils.getUserIdFromJwt(jwtUtils.getJwt(request));
		UserReportDto user = userService.getReportUser(userId);
		
		ReportGenerator reportGenerator = new ReportGenerator(user);
		reportGenerator.export(response);
		
		
	}
}
