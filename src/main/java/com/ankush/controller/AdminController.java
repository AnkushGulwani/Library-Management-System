package com.ankush.controller;

import com.ankush.model.Role;
import com.ankush.model.User;
import com.ankush.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/api/admin")
@CrossOrigin("*")
public class AdminController {

    @Autowired
    private UserService service;

    // ── GET: all pending (unverified) users ──────────────────────────
    @GetMapping("/pending-users")
    public ResponseEntity<List<User>> getPendingUsers() {
        return ResponseEntity.ok(service.getPendingUsers());
    }

    // ── GET: all verified users ──────────────────────────────────────
    @GetMapping("/verified-users")
    public ResponseEntity<List<User>> getVerifiedUsers() {
        return ResponseEntity.ok(service.getVerifiedUsers());
    }

    // ── GET: dashboard stats ─────────────────────────────────────────
    @GetMapping("/stats")
    public ResponseEntity<Map<String, Long>> getStats() {
        List<User> pending  = service.getPendingUsers();
        List<User> verified = service.getVerifiedUsers();
        long total          = pending.size() + verified.size();

        Map<String, Long> stats = new HashMap<>();
        stats.put("total",    total);
        stats.put("pending",  (long) pending.size());
        stats.put("verified", (long) verified.size());
        return ResponseEntity.ok(stats);
    }

    // ── POST: verify user (generate temp password + email) ───────────
    @PostMapping("/verify/{id}")
    public ResponseEntity<String> verifyUser(@PathVariable Long id) {
        try {
            User user = service.findById(id);
            if (user == null) return ResponseEntity.notFound().build();

            String tempPassword = UUID.randomUUID().toString().substring(0, 8);
            user.setPassword(tempPassword);
            user.setVerified(true);
            user.setFirstLogin(true);
            service.save(user);

            try {
                service.sendPasswordEmail(user.getEmail(), tempPassword);
            } catch (Exception e) {
                System.out.println("Email failed but user verified.");
                e.printStackTrace();
            }

            return ResponseEntity.ok("User verified.");
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(500).body("Verification failed.");
        }
    }

    // ── DELETE: reject / remove a pending user ───────────────────────
    @DeleteMapping("/reject/{id}")
    public ResponseEntity<String> rejectUser(@PathVariable Long id) {
        try {
            User user = service.findById(id);
            if (user == null) return ResponseEntity.notFound().build();

            service.deleteUser(id);
            return ResponseEntity.ok("User rejected and removed.");
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(500).body("Rejection failed.");
        }
    }

    // ── PUT: change a user's role (USER ↔ LIBRARIAN) ─────────────────
    @PutMapping("/user/{id}/role")
    public ResponseEntity<String> changeRole(
            @PathVariable Long id,
            @RequestParam String role) {
        try {
            User user = service.findById(id);
            if (user == null) return ResponseEntity.notFound().build();

            user.setRole(Role.valueOf(role.toUpperCase()));
            service.save(user);
            return ResponseEntity.ok("Role updated to " + role);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body("Invalid role: " + role);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(500).body("Role update failed.");
        }
    }
}