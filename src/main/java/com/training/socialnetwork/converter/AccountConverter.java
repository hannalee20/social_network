package com.training.socialnetwork.converter;

import org.springframework.stereotype.Component;

import com.training.socialnetwork.dto.AccountDTO;
import com.training.socialnetwork.entity.AccountEntity;

@Component
public class AccountConverter {

	public AccountDTO toDto(AccountEntity accountEntity) {
		AccountDTO result = new AccountDTO();
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
