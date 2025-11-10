package com.kalex.hosdoc_auth.filter;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import com.kalex.hosdoc_auth.service.JwtService;
import com.kalex.hosdoc_auth.service.MyUserDetailsService;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class JwtFilter extends OncePerRequestFilter {

	@Autowired
	private JwtService jwtService;
	@Autowired
	private MyUserDetailsService service;

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
			throws ServletException, IOException {
		String authorizationHeader = request.getHeader("Authorization");
		String token = null;
		String userName = null;
		// Ex. Bearer
		// eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJ1c2VyMSIsImV4cCI6MTU5NjE0ODk3NCwiaWF0IjoxNTk2MTEyOTc0fQ.kZK6iSzga5c-JsMiJ3TSVrDwDx_nLuhQwBvsjWqIfw0
		if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
			token = authorizationHeader.substring(7);
			// Validate token format before processing
			if (isValidJwtFormat(token)) {
				try {
					userName = jwtService.extractUsername(token);
				} catch (Exception e) {
					// Log the error but don't throw exception to avoid breaking the filter chain
					System.err.println("Error extracting username from JWT token: " + e.getMessage());
					// Continue without authentication
				}
			}
		}
		if (userName != null && SecurityContextHolder.getContext().getAuthentication() == null) {
			try {
				UserDetails userDetails = service.loadUserByUsername(userName);
				if (jwtService.validateToken(token, userDetails)) {
					UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken = new UsernamePasswordAuthenticationToken(
							userDetails, null, userDetails.getAuthorities());
					usernamePasswordAuthenticationToken
							.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
					SecurityContextHolder.getContext().setAuthentication(usernamePasswordAuthenticationToken);
				}
			} catch (Exception e) {
				// Log the error but don't throw exception to avoid breaking the filter chain
				System.err.println("Error validating JWT token: " + e.getMessage());
				// Continue without authentication
			}
		}
		filterChain.doFilter(request, response);

	}
	
	/**
	 * Validates if the token has the basic JWT format (contains exactly 2 periods)
	 * @param token the token to validate
	 * @return true if the token has valid JWT format, false otherwise
	 */
	private boolean isValidJwtFormat(String token) {
		if (token == null || token.trim().isEmpty()) {
			return false;
		}
		// Count the number of periods in the token
		long periodCount = token.chars().filter(ch -> ch == '.').count();
		// JWT should have exactly 2 periods (header.payload.signature)
		return periodCount == 2;
	}

}


