package com.ankush.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.ankush.model.MembershipPlan;

public interface MembershipPlanRepository extends JpaRepository<MembershipPlan, Long> {
}
