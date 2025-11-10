package com.kalex.hosdoc_auth.dao;

import com.kalex.hosdoc_auth.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserDao extends JpaRepository<User, Integer> {
	Optional<User> findByUsername(String username);
	Optional<User> findById(Integer id);
	boolean existsByUsername(String username);
	
	// Custom query to handle duplicates - returns first result if multiple exist
	@Query("SELECT u FROM User u WHERE u.username = :username ORDER BY u.id ASC")
	Optional<User> findFirstByUsername(@Param("username") String username);
}
