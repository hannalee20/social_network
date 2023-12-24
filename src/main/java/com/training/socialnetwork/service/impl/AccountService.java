package com.training.socialnetwork.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.training.socialnetwork.dto.AccountDTO;
import com.training.socialnetwork.entity.AccountEntity;
import com.training.socialnetwork.repository.AccountRepository;
import com.training.socialnetwork.service.IAccountService;

@Service
public class AccountService implements IAccountService {

	@Autowired
	private AccountRepository accountRepository;
	
	@Override
	public AccountDTO createAccount(AccountDTO accountDTO) {
		AccountEntity accountEntity = new AccountEntity();
		return accountDTO;
	}

}
