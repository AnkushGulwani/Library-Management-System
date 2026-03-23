package com.ankush.service;

import java.time.LocalDate;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ankush.model.MembershipPlan;
import com.ankush.model.User;
import com.ankush.repository.MembershipPlanRepository;
import com.ankush.repository.UserRepository;

@Service
public class MembershipService {

    @Autowired
    private MembershipPlanRepository planRepository;

    @Autowired
    private UserRepository userRepository;

    public MembershipPlan addPlan(MembershipPlan plan) {
        return planRepository.save(plan);
    }

    public List<MembershipPlan> getAllPlans() {
        return planRepository.findAll();
    }

    public User assignPlan(Long userId, Long planId) {

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        MembershipPlan plan = planRepository.findById(planId)
                .orElseThrow(() -> new RuntimeException("Plan not found"));

        user.setMembershipPlan(plan);
        user.setMembershipStartDate(LocalDate.now());
        user.setMembershipExpiryDate(
                LocalDate.now().plusDays(plan.getDurationInDays())
        );

        return userRepository.save(user);
    }
    public List<User> getAllUsersWithMembershipStatus() {

        List<User> users = userRepository.findAll();

        users.forEach(user -> {

            if (user.getMembershipPlan() == null) {
                user.setMembershipStartDate(null);
                user.setMembershipExpiryDate(null);
            }

        });

        return users;
    }
   
}