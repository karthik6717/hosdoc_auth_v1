package com.kalex.hosdoc_auth.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.kalex.hosdoc_auth.dao.UserDao;
import com.kalex.hosdoc_auth.entity.User;
import com.kalex.hosdoc_auth.model.UserPrincipal;

@Service
public class MyUserDetailsService implements UserDetailsService{

	@Autowired
	UserDao userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        // Use findFirstByUsername to handle duplicate usernames
        User user = userRepository.findFirstByUsername(username)
        		.orElseThrow(() -> {
        			System.out.println("user not found: " + username);
        			return new UsernameNotFoundException("User not found: " + username);
        		});

        return new UserPrincipal(user);
    }
}


