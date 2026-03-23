package com.ankush.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import com.ankush.model.MembershipPlan;
import com.ankush.model.Role;
import com.ankush.model.User;
import com.ankush.repository.UserRepository;

@Service
public class UserService {

    @Autowired
    private UserRepository repo;

    @Autowired
    private JavaMailSender mailSender;

    // ================= REGISTER =================
    public User register(User user) {
        return repo.save(user);
    }

    // ================= LOGIN =================
    public User login(String email, String password) {
        User user = repo.findByEmail(email);
        if (user == null)            return null;
        if (!user.getVerified())     return null;
        if (!user.getPassword().equals(password)) return null;
        return user;
    }

    // ================= FIND BY ID =================
    public User findById(Long id) {
        return repo.findById(id).orElse(null);
    }

    // ================= SAVE USER =================
    public User save(User user) {
        return repo.save(user);
    }

    // ================= GET PENDING USERS (unverified) =================
    public List<User> getPendingUsers() {
        return repo.findByVerifiedFalse();
    }

    // ================= GET VERIFIED USERS =================
    // Used by AdminController GET /api/admin/verified-users
    public List<User> getVerifiedUsers() {
        return repo.findByVerifiedTrue();
    }

    // ================= DELETE USER (reject) =================
    // Used by AdminController DELETE /api/admin/reject/{id}
    public void deleteUser(Long id) {
        repo.deleteById(id);
    }

    // ================= GET USERS BY ROLE =================
    // Used by MembershipController and UserController
    public List<User> getUsersByRole(Role role) {
        return repo.findByRole(role);
    }

    // ================= GET MEMBERSHIP =================
    public MembershipPlan getUserMembership(Long userId) {
        User user = repo.findById(userId).orElseThrow();
        return user.getMembershipPlan();
    }

    // ================= SEND PASSWORD EMAIL =================
    public void sendPasswordEmail(String toEmail, String tempPassword) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom("ankushgulwani18@gmail.com");
        message.setTo(toEmail);
        message.setSubject("Library Account Approved");
        message.setText(
            "Dear User,\n\n" +
            "Greetings from the Library Management System.\n\n" +
            "Your account has been approved. Your temporary password is:\n\n" +
            "    " + tempPassword + "\n\n" +
            "Please log in and change your password at your earliest convenience.\n\n" +
            "Best regards,\n" +
            "Library Management System"
        );
        mailSender.send(message);
    }

    // ================= CHANGE PASSWORD (first login) =================
    public String changePassword(String email, String newPassword) {
        User user = repo.findByEmail(email);
        if (user == null) return "User not found.";
        user.setPassword(newPassword);
        user.setFirstLogin(false);
        repo.save(user);
        return "Password changed successfully.";
    }

    // ================= CHANGE PASSWORD (from dashboard) =================
    public String changePasswordFromDashboard(String email, String currentPassword, String newPassword) {
        User user = repo.findByEmail(email);
        if (user == null)                                  return "User not found.";
        if (!user.getPassword().equals(currentPassword))   return "Current password is incorrect.";
        user.setPassword(newPassword);
        repo.save(user);
        return "Password updated successfully";
    }
}