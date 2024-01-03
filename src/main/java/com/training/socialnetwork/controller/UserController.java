package com.training.socialnetwork.controller;

import java.util.ArrayList;
import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.training.socialnetwork.entity.User;
import com.training.socialnetwork.service.IUserService;

@RestController
@RequestMapping(value = "/user")
public class UserController {
	
	@Autowired
	private IUserService userService;
	
	@Autowired
	private ObjectMapper objectMapper;

	@RequestMapping(value = "/user", method = RequestMethod.GET)
	public List<String> getProducts() {
	      List<String> productsList = new ArrayList<>();
	      productsList.add("Honey");
	      productsList.add("Almond");
	      return productsList;
	   }
	
	@PostMapping(value = "/register")
	public ResponseEntity<User> registerUser(@RequestBody User user) {
		return new ResponseEntity<User>(userService.createUser(user), HttpStatus.CREATED);
	}
	
	@PostMapping(value = "/login")
	public ResponseEntity<User> loginUser(@RequestParam("username") String username, @RequestParam("password") String password) {
		boolean result = userService.loginUser(username, password);
		if(result) {
			return new ResponseEntity<User>(HttpStatus.OK);
		} else {
			return new ResponseEntity<User>(HttpStatus.valueOf("Invalid"));
		}
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
	
	@GetMapping(value = "/detaul/{userId}")
	public ResponseEntity<Object> getUserInfo(@PathVariable(value = "userId") int userId) throws Exception {
		User user = userService.getInfo(userId);
		
		return new ResponseEntity<>(objectMapper.writeValueAsString(user), HttpStatus.OK);
	}
}
