package com.training.socialnetwork.converter;

import org.springframework.stereotype.Component;

import com.training.socialnetwork.dto.UserDTO;
import com.training.socialnetwork.entity.UserEntity;

@Component
public class UserConverter {

	public UserDTO toDto(UserEntity userEntity) {
		UserDTO result = new UserDTO();
		result.setUserId(userEntity.getUserId());
		result.setUsername(userEntity.getUsername());
		result.setPassword(userEntity.getPassword());
		result.setEmail(userEntity.getEmail());
		result.setRole(userEntity.getRole());
		result.setCreateDate(userEntity.getCreateDate());
		result.setUpdateDate(userEntity.getUpdateDate());
		return result;
	}
	
}
