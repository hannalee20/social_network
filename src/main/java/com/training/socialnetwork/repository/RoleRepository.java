package com.training.socialnetwork.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.training.socialnetwork.entity.Role;

public interface RoleRepository  extends JpaRepository<Role, Integer> {

	Role findByName(String name);
}
