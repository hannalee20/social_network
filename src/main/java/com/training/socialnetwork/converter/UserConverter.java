package com.training.socialnetwork.converter;

import org.springframework.stereotype.Component;

import com.training.socialnetwork.dto.UserDTO;
import com.training.socialnetwork.entity.AccountEntity;

@Component
public class UserConverter {

	public UserDTO toDto(AccountEntity accountEntity) {
		UserDTO result = new UserDTO();
		result.setAccountId(accountEntity.getAccountId());
		result.setUsername(accountEntity.getUsername());
		result.setPassword(accountEntity.getPassword());
		result.setEmail(accountEntity.getEmail());
		result.setRole(accountEntity.getRole());
		result.setCreateDate(accountEntity.getCreateDate());
		result.setUpdateDate(accountEntity.getUpdateDate());
		return result;
	}
	
}
