package com.training.socialnetwork.security;

import java.util.Date;

import org.springframework.stereotype.Component;

import com.training.socialnetwork.dto.request.user.CustomUserDetails;

import io.jsonwebtoken.Jwts;

@Component
public class JwtTokenProvider {

	private final String JWT_SECRET = "test";
	
	private final long JWT_EXPIRATION = 604800000L;
	
	public String generateToken(CustomUserDetails userDetails) {
		Date now = new Date();
		Date expiryDate = new Date(now.getTime() + JWT_EXPIRATION);
		
//		return Jwts.builder()
//				.setSubject(Long.toString(userDetails.))
	}
}
