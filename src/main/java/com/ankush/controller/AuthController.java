package com.ankush.controller;

import com.ankush.model.Role;
import com.ankush.model.User;
import com.ankush.service.UserService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.nio.file.*;
import java.util.Map;

@RestController
@RequestMapping("/api/auth")
@CrossOrigin("*")
public class AuthController {

	@Autowired
	private UserService service;

	// ================= REGISTER =================
	@PostMapping("/register")
	public ResponseEntity<String> register(
	        @ModelAttribute User user,
	        @RequestParam("idProofFile") MultipartFile idProofFile) {

	    try {

	        if (idProofFile.isEmpty()
	                || !"application/pdf".equals(idProofFile.getContentType())) {
	            return ResponseEntity.badRequest()
	                    .body("Only PDF file allowed.");
	        }

	        Path uploadPath = Paths.get("C:/uploads/idproofs");
	        Files.createDirectories(uploadPath);

	        String fileName = System.currentTimeMillis()
	                + "_" + idProofFile.getOriginalFilename();

	        Path filePath = uploadPath.resolve(fileName);
	        idProofFile.transferTo(filePath.toFile());

	        user.setIdProofPath(filePath.toString());

	        // 🔥 IMPORTANT FIX
	        user.setRole(Role.USER);   // default role

	        user.setVerified(false);
	        user.setFirstLogin(true);
	        user.setPassword(null);

	        service.register(user);

	        return ResponseEntity.ok("Registration submitted successfully.");

	    } catch (Exception e) {
	        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
	                .body("Error while uploading file." + e.getMessage());
	    }
	}

	// ================= LOGIN =================

	@PostMapping("/login")
	public ResponseEntity<?> login(@RequestBody User loginRequest) {

		User user = service.login(loginRequest.getEmail(), loginRequest.getPassword());

		if (user == null) {
			return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid credentials or not verified.");
		}

		// Remove password before sending response
		user.setPassword(null);

		return ResponseEntity.ok(user);
	}

	// ================= CHANGE PASSWORD =================
	@PostMapping("/change-password")
	public ResponseEntity<String> changePassword(@RequestBody Map<String, String> request) {

		String email = request.get("email");
		String newPassword = request.get("newPassword");

		service.changePassword(email, newPassword);

		return ResponseEntity.ok("Password updated successfully");
	}

	@PostMapping("/change-password-two")
	public ResponseEntity<String> changePasswordTwo(@RequestBody Map<String, String> request) {

		String email = request.get("email");
		String currentPassword = request.get("currentPassword");
		String newPassword = request.get("newPassword");

		String response = service.changePasswordFromDashboard(email, currentPassword, newPassword);

		if (response.equals("Password updated successfully")) {
			return ResponseEntity.ok(response);
		}

		return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
	}
}
