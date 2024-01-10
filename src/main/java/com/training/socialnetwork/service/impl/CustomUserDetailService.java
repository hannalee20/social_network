package com.training.socialnetwork.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.training.socialnetwork.dto.request.user.CustomUserDetail;
import com.training.socialnetwork.entity.User;
import com.training.socialnetwork.repository.UserRepository;
import com.training.socialnetwork.util.constant.Constant;

@Service
public class CustomUserDetailService implements UserDetailsService {

	@Autowired
	private UserRepository userRepository;
	
	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		User user = userRepository.findByUsername(username);
		
		if(user == null) {
			throw new UsernameNotFoundException(Constant.SERVER_ERROR);
		}
		
		return CustomUserDetail.build(user);
	}

}
