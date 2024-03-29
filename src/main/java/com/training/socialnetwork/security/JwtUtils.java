package com.training.socialnetwork.security;

import java.security.Key;
import java.util.Date;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import com.training.socialnetwork.dto.request.user.CustomUserDetail;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.UnsupportedJwtException;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;

@Component
public class JwtUtils {

	@Value("${social-network.app.jwtSecret}")
	private String jwtSecret;

	@Value("${social-network.app.jwtCookieName}")
	private String jwtCookie;

	@Value("${social-network.app.jwtExpirationMs}")
	private String jwtExpirationMs;

	public String generateToken(Authentication authentication) {
		CustomUserDetail customUserDetail = (CustomUserDetail) authentication.getPrincipal();
		long miliSec = System.currentTimeMillis() + Long.parseLong(jwtExpirationMs);
		Date expiryDate  = new Date(miliSec);
		
		return Jwts.builder()
				.setSubject(Integer.toString(customUserDetail.getUserId()))
				.setIssuedAt(new Date())
				.setExpiration(expiryDate)
				.signWith(key(), SignatureAlgorithm.HS256)
				.compact();
	}
	
	public int getUserIdFromJwt(String token) {
		Claims claims = Jwts.parserBuilder()
				.setSigningKey(key())
				.build()
				.parseClaimsJws(token)
				.getBody();
		
		return Integer.parseInt(claims.getSubject());
	}
	
	public String getJwt(HttpServletRequest request) {
		String jwt = request.getHeader("Authorization");

		if (jwt != null && jwt.startsWith("Bearer ")) {
            return jwt.substring(7);
        }
        return null;
	}

	public boolean validateJwtToken(String authToken) throws Exception {
		try {
			Jwts.parserBuilder().setSigningKey(key()).build().parse(authToken);
			return true;
		} catch (MalformedJwtException e) {
			throw new Exception("Invalid JWT token: {}" + e.getMessage());
		} catch (ExpiredJwtException e) {
			throw new Exception("JWT token is expired: {}" + e.getMessage());
		} catch (UnsupportedJwtException e) {
			throw new Exception("JWT token is unsupported: {}" + e.getMessage());
		} catch (IllegalArgumentException e) {
			throw new Exception("JWT claims string is empty: {}" + e.getMessage());
		}
	}

	public Key key() {
		return Keys.hmacShaKeyFor(Decoders.BASE64.decode(jwtSecret));
	}

}
