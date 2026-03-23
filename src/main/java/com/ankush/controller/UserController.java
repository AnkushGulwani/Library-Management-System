package com.ankush.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ankush.model.Issue;
import com.ankush.model.MembershipPlan;
import com.ankush.model.Role;
import com.ankush.model.User;
import com.ankush.service.IssueService;
import com.ankush.service.MembershipService;
import com.ankush.service.UserService;

@RestController
@RequestMapping("/user")
@CrossOrigin
public class UserController {

    @Autowired
    private IssueService issueService;

    @GetMapping("/{userId}/issued-books")
    public List<Issue> issuedBooks(@PathVariable Long userId) {
        return issueService.getIssuedBooks(userId);
    }

    @GetMapping("/{userId}/history")
    public List<Issue> readingHistory(@PathVariable Long userId) {
        return issueService.getReadingHistory(userId);
    }

    @GetMapping("/{userId}/pending-fines")
    public List<Issue> pendingFines(@PathVariable Long userId) {
        return issueService.getPendingFines(userId);
    }

    @GetMapping("/{userId}/total-fine")
    public double totalFine(@PathVariable Long userId) {
        return issueService.getTotalFine(userId);
    }
    @Autowired
    private UserService userService;

    @Autowired
    private MembershipService membershipService;

    @GetMapping("/{userId}/membership")
    public MembershipPlan userMembership(@PathVariable Long userId) {
        return userService.getUserMembership(userId);
    }

    @GetMapping("/membership/all")
    public List<MembershipPlan> allPlans() {
        return membershipService.getAllPlans();
    }
    @GetMapping("/members")
    public List<User> getMembers() {
        return userService.getUsersByRole(Role.USER);
    }
    
}
