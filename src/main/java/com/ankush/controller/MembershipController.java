package com.ankush.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ankush.model.MembershipPlan;
import com.ankush.model.Role;
import com.ankush.model.User;
import com.ankush.service.MembershipService;
import com.ankush.service.UserService;

@RestController
@RequestMapping("/api")
@CrossOrigin
public class MembershipController {

    @Autowired
    private MembershipService membershipService;
    
    @Autowired
    private UserService userService;

    // ADD PLAN (ADMIN)
    @PostMapping("/plans")
    public MembershipPlan addPlan(@RequestBody MembershipPlan plan) {
        return membershipService.addPlan(plan);
    }

    // GET ALL PLANS
    @GetMapping("/plans")
    public List<MembershipPlan> getPlans() {
        return membershipService.getAllPlans();
    }

    // ASSIGN PLAN TO USER
    @PostMapping("/members/{userId}/assign-plan/{planId}")
    public User assignPlan(@PathVariable Long userId,
                           @PathVariable Long planId) {
        return membershipService.assignPlan(userId, planId);
    }
    
    @GetMapping("/membership-status")
    public List<User> getMembershipStatus() {
        return userService.getUsersByRole(Role.USER);
    }
}