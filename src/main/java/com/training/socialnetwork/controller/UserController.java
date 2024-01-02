package com.training.socialnetwork.controller;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.training.socialnetwork.entity.User;
import com.training.socialnetwork.service.impl.UserService;

@RestController
@RequestMapping(value = "/user")
public class UserController {
	
	@Autowired
	private UserService userService;

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
	
	@PostMapping(value = "/update")
	public void updateUser() {
		
	}
}
