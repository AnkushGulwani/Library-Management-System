package com.ankush.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.ankush.model.Role;
import com.ankush.model.User;

public interface UserRepository extends JpaRepository<User, Long> {

	// Find user by email (used in login, changePassword, etc.)
	User findByEmail(String email);

	// Find all users who are not yet verified (used in AdminController)
	List<User> findByVerifiedFalse();

	Optional<User> findByFirstNameIgnoreCase(String firstName);

	Optional<User> findByEmailIgnoreCase(String email);

	List<User> findByMembershipPlanIsNotNull();

	List<User> findByRole(Role role);

	// Used by AdminController — GET /api/admin/verified-users ← NEW
	List<User> findByVerifiedTrue();

}