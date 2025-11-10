package com.kalex.hosdoc_auth.service;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

@Service
public class JwtService {
	
	// In production, this should be loaded from environment variables or config
	private static final String SECRET_KEY = "404E635266556A586E3272357538782F413F4428472B4B6250645367566B5970";
	
	private SecretKey getSigningKey() {
		byte[] keyBytes = SECRET_KEY.getBytes(StandardCharsets.UTF_8);
		return Keys.hmacShaKeyFor(keyBytes);
	}
	
	public String extractUsername(String token) {
		return extractClaim(token, Claims::getSubject);
	}
	
	public Date extractExpiration(String token) {
		return extractClaim(token, Claims::getExpiration);
	}
	
	public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
		final Claims claims = extractAllClaims(token);
		return claimsResolver.apply(claims);
	}
	
	private Claims extractAllClaims(String token) {
		try {
			return Jwts.parser()
				.verifyWith(getSigningKey())
				.build()
				.parseSignedClaims(token)
				.getPayload();
		} catch (Exception e) {
			throw new RuntimeException("Error parsing JWT token: " + e.getMessage(), e);
		}
	}
	
	private Boolean isTokenExpired(String token) {
		return extractExpiration(token).before(new Date());
	}
	
	public String generateToken(String username, com.kalex.hosdoc_auth.model.Role role, Long userId) {
		Map<String, Object> claims = new HashMap<>();
		claims.put("role", role.toString());
		claims.put("userId", userId);
		
		return Jwts.builder()
			.claims()
			.add(claims)
			.subject(username)
			.issuedAt(new Date(System.currentTimeMillis()))
			.expiration(new Date(System.currentTimeMillis() + 60 * 60 * 1000)) // 1 hour
			.and()
			.signWith(getSigningKey())
			.compact();
	}
	
	public String generateToken(UserDetails userDetails) {
		Map<String, Object> claims = new HashMap<>();
		return createToken(claims, userDetails.getUsername());
	}
	
	public String generateToken(UserDetails userDetails, Map<String, Object> extraClaims) {
		Map<String, Object> claims = new HashMap<>(extraClaims);
		// Add role to claims
		if (!userDetails.getAuthorities().isEmpty()) {
			claims.put("role", userDetails.getAuthorities().iterator().next().getAuthority());
		}
		return createToken(claims, userDetails.getUsername());
	}
	
	public SecretKey getKey() {
		return getSigningKey();
	}
	
	private String createToken(Map<String, Object> claims, String subject) {
		return Jwts.builder()
			.claims(claims)
			.subject(subject)
			.issuedAt(new Date(System.currentTimeMillis()))
			.expiration(new Date(System.currentTimeMillis() + 1000 * 60 * 60 * 10)) // 10 hours
			.signWith(getSigningKey())
			.compact();
	}
	
	public Boolean validateToken(String token, UserDetails userDetails) {
		try {
			final String username = extractUsername(token);
			return (username.equals(userDetails.getUsername()) && !isTokenExpired(token));
		} catch (Exception e) {
			return false;
		}
	}
}
