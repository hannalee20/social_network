package com.training.socialnetwork.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.training.socialnetwork.entity.AccountEntity;


public interface AccountRepository extends JpaRepository<AccountEntity, Integer>{

}
