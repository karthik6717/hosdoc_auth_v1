package com.kalex.hosdoc_auth.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import com.kalex.hosdoc_auth.entity.User;
import com.kalex.hosdoc_auth.service.JwtService;
import com.kalex.hosdoc_auth.service.UserServiceImpl;

import io.jsonwebtoken.Jwts;
import jakarta.ws.rs.core.HttpHeaders;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping(value = "/api/auth")
@RequiredArgsConstructor
@CrossOrigin("*")
public class UserController {

	private final UserServiceImpl userService;
	
	@Autowired
	JwtService jwtService;

	@PostMapping("/validate")
	public ResponseEntity<Void> validateToken(@RequestHeader(HttpHeaders.AUTHORIZATION) String authorizationHeader) {
		try {
			if (authorizationHeader == null || !authorizationHeader.startsWith("Bearer ")) {
				return ResponseEntity.status(401).build(); // Invalid authorization header
			}
			
			String token = authorizationHeader.substring(7);
			
			// Validate token format before parsing
			if (token == null || token.trim().isEmpty()) {
				return ResponseEntity.status(401).build(); // Empty token
			}
			
			// Check if token has proper JWT format (exactly 2 periods)
			long periodCount = token.chars().filter(ch -> ch == '.').count();
			if (periodCount != 2) {
				return ResponseEntity.status(401).build(); // Invalid JWT format
			}
			
			// Parse and verify the token
			Jwts.parser()
	                .verifyWith(jwtService.getKey())
	                .build()
	                .parseSignedClaims(token);
			return ResponseEntity.ok().build();
		} catch (Exception e) {
			// Log the error for debugging
			System.err.println("Error validating JWT token: " + e.getMessage());
			return ResponseEntity.status(401).build(); // Invalid token
		}
	}

	@PostMapping("/register")
	public ResponseEntity<?> register(@RequestBody User user) {
		try {
			User registeredUser = userService.register(user);
			return new ResponseEntity<>(registeredUser, HttpStatus.CREATED);
		} catch (com.kalex.hosdoc_auth.exception.UserAlreadyExistsException e) {
			return ResponseEntity.status(HttpStatus.CONFLICT)
				.body(new ErrorResponse(HttpStatus.CONFLICT.value(), "Conflict", e.getMessage(), "/api/auth/register"));
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST)
				.body(new ErrorResponse(HttpStatus.BAD_REQUEST.value(), "Bad Request", e.getMessage(), "/api/auth/register"));
		}
	}

	@PostMapping("/login")
	public String login(@RequestParam("username") String username, @RequestParam("password") String password) {
		return userService.verifyAndGenerateToken(username, password);
	}

	@GetMapping("/users")
	public ResponseEntity<List<User>> getAllUsers() {
		try {
			List<User> userList = userService.getAllUsers();
			return new ResponseEntity<>(userList, HttpStatus.OK);
		} catch (Exception e) {
			throw new ResponseStatusException(HttpStatus.EXPECTATION_FAILED, e.getMessage());
		}
	}

	@GetMapping("/users/{id}")
	public ResponseEntity<User> getUserById(@PathVariable("id") Integer id) {
		try {
			User user = userService.getUserById(id);
			return new ResponseEntity<>(user, HttpStatus.OK);
		} catch (Exception e) {
			throw new ResponseStatusException(HttpStatus.EXPECTATION_FAILED, e.getMessage());
		}
	}

	@GetMapping("/me")
	public ResponseEntity<User> getCurrentUser(@RequestHeader(HttpHeaders.AUTHORIZATION) String authorizationHeader) {
		try {
			if (authorizationHeader == null || !authorizationHeader.startsWith("Bearer ")) {
				return ResponseEntity.status(401).build();
			}
			
			String token = authorizationHeader.substring(7);
			String username = jwtService.extractUsername(token);
			
			if (username == null) {
				return ResponseEntity.status(401).build();
			}
			
			User user = userService.getUserByUsername(username);
			return new ResponseEntity<>(user, HttpStatus.OK);
		} catch (Exception e) {
			throw new ResponseStatusException(HttpStatus.EXPECTATION_FAILED, e.getMessage());
		}
	}

	@PreAuthorize("hasRole('ADMIN')")
	@GetMapping("/ping")
	public ResponseEntity<String> test() {
		try {
			return new ResponseEntity<>("Welcome", HttpStatus.OK);
		} catch (Exception e) {
			throw new ResponseStatusException(HttpStatus.EXPECTATION_FAILED, e.getMessage());
		}
	}
	
	// Error response DTO
	@Getter
	@Setter
	@NoArgsConstructor
	@AllArgsConstructor
	private static class ErrorResponse {
		private int status;
		private String error;
		private String message;
		private String path;
	}
}
