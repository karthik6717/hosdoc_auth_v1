package com.kalex.hosdoc_auth.service;

import java.util.List;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.kalex.hosdoc_auth.dao.UserDao;
import com.kalex.hosdoc_auth.entity.User;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UserServiceImpl {
	
	private final UserDao userRepository;
    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;
    private final BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder(12);

    public User register(User user) {
    	// Check if username already exists
    	if (userRepository.existsByUsername(user.getUsername())) {
    		throw new com.kalex.hosdoc_auth.exception.UserAlreadyExistsException(
    			"User with username '" + user.getUsername() + "' already exists");
    	}
    	
    	user.setPassword(bCryptPasswordEncoder.encode(user.getPassword()));
        return userRepository.save(user);
    }

    public String verifyAndGenerateToken(String username, String password) {
        Authentication authentication = authenticationManager
                .authenticate(new UsernamePasswordAuthenticationToken(username, password));

        if (authentication.isAuthenticated()) {
        	User user = userRepository.findFirstByUsername(username)
        			.orElseThrow(() -> new RuntimeException("User not found after authentication"));
            return jwtService.generateToken(username, user.getRole(), user.getUserId());
        }

        return "fail";
    }
    
    public List<User> getAllUsers(){
    	return userRepository.findAll();
    }
    
    public User getUserById(Integer id) throws Exception {
    	return userRepository.findById(id)
				.orElseThrow(() -> new Exception("Invalid Id"));
    }
    
    public User getUserByUsername(String username) throws Exception {
    	return userRepository.findFirstByUsername(username)
    			.orElseThrow(() -> new Exception("User not found: " + username));
    }
}
