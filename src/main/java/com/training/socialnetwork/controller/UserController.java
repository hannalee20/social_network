package com.training.socialnetwork.controller;

import javax.validation.Valid;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.training.socialnetwork.dto.request.user.UserRegisterDto;
import com.training.socialnetwork.dto.response.user.UserRegistedDto;
import com.training.socialnetwork.entity.User;
import com.training.socialnetwork.service.IUserService;

@RestController
@RequestMapping(value = "/user")
public class UserController {
	
	@Autowired
	private IUserService userService;
	
	@Autowired
	private ObjectMapper objectMapper;
	
	@Autowired
	private ModelMapper modelMapper;
	
	@PostMapping(value = "/register")
	public ResponseEntity<Object> registerUser(@RequestBody UserRegisterDto userRegisterDto) throws Exception {
		User user = modelMapper.map(userRegisterDto, User.class);
		UserRegistedDto result = userService.createUser(user);
		return new ResponseEntity<Object>(result, HttpStatus.CREATED);
	}
	
	@PostMapping(value = "/login")
	public ResponseEntity<Object> loginUser(@RequestParam("username") String username, @RequestParam("password") String password) throws Exception {
		return new ResponseEntity<Object>(userService.loginUser(username, password), HttpStatus.OK);
	}
	
	@PutMapping(value = "/update/{userId}")
	public ResponseEntity<User> updateUser(@RequestBody @Valid User user, @PathVariable(value = "userId") int userId) {
		boolean result = userService.updateInfo(user, userId);
		if(result) {
			return new ResponseEntity<User>(HttpStatus.OK);
		} else {
			return new ResponseEntity<User>(HttpStatus.valueOf("Invalid"));
		}
	}
	
	@GetMapping(value = "/detail/{userId}")
	public ResponseEntity<Object> getUserInfo(@PathVariable(value = "userId") int userId) throws Exception {
		User user = userService.getInfo(userId);
		
		return new ResponseEntity<>(objectMapper.writeValueAsString(user), HttpStatus.OK);
	}
}
