package com.training.socialnetwork.security;

import java.security.Key;
import java.text.ParseException;
import java.util.Date;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseCookie;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;
import org.springframework.web.util.WebUtils;

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
				.signWith(key(), SignatureAlgorithm.HS512)
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
	
	public String getJwtFromCookies(HttpServletRequest request) {
		Cookie cookie = WebUtils.getCookie(request, jwtCookie);

		if (cookie != null) {
			return cookie.getValue();
		} else {
			return null;
		}
	}

	public ResponseCookie generateJwtCookie(CustomUserDetail userPrincipal) throws ParseException {
		String jwt = generateTokenFromUsername(userPrincipal.getUsername());
		ResponseCookie cookie = ResponseCookie.from(jwtCookie, jwt).path("/user").maxAge(24 * 60 * 60).httpOnly(true)
				.build();

		return cookie;
	}

	public ResponseCookie getCleanJwtCookie() {
		ResponseCookie cookie = ResponseCookie.from(jwtCookie, null).path("/api").build();

		return cookie;
	}

	public String getUsernameFromJwtToken(String token) {
		return Jwts.parserBuilder().setSigningKey(key()).build().parseClaimsJws(token).getBody().getSubject();
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

	private Key key() {
		return Keys.hmacShaKeyFor(Decoders.BASE64.decode(jwtSecret));
	}

	private String generateTokenFromUsername(String username) throws ParseException {
		long miliSec = System.currentTimeMillis() + Long.parseLong(jwtExpirationMs);
		Date dateTime = new Date(miliSec);
		return Jwts.builder().setSubject(username).setIssuedAt(new Date()).setExpiration(dateTime)
				.signWith(key(), SignatureAlgorithm.HS256).compact();
	}
}
