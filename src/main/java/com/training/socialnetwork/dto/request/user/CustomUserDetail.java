package com.training.socialnetwork.dto.request.user;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import com.training.socialnetwork.entity.Role;
import com.training.socialnetwork.entity.User;


public class CustomUserDetail implements UserDetails{

	private static final long serialVersionUID = 1L;
	
	private int userId;
	private String username;
	private String password;
	private Collection<? extends GrantedAuthority> authorities;

	 
	public CustomUserDetail(int userId, String username, String password,
			Collection<? extends GrantedAuthority> authorities) {
		this.userId = userId;
		this.username = username;
		this.password = password;
		this.authorities = authorities;
	}

	public static CustomUserDetail build(User user) {
		Role role = user.getRole();
		List<GrantedAuthority> authorities = new ArrayList<>();
		authorities.add(new SimpleGrantedAuthority(role.getName()));
		
		return new CustomUserDetail(user.getUserId(), user.getUsername(), user.getPassword(), authorities);
	}
	
	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		return authorities;
	}
	
	public int getUserId() {
		return userId;
	}
	
	@Override
	public String getPassword() {
		return password;
	}

	@Override
	public String getUsername() {
		return username;
	}

	@Override
	public boolean isAccountNonExpired() {
		return true;
	}

	@Override
	public boolean isAccountNonLocked() {
		return true;
	}

	@Override
	public boolean isCredentialsNonExpired() {
		return true;
	}

	@Override
	public boolean isEnabled() {
		return true;
	}

}
