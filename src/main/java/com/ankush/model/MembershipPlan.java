package com.ankush.model;

import jakarta.persistence.*;

@Entity
@Table(name = "membership_plans")
public class MembershipPlan {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String planName;
    private Integer durationInDays;
    private Double price;
    private Integer maxBooksAllowed;
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getPlanName() {
		return planName;
	}
	public void setPlanName(String planName) {
		this.planName = planName;
	}
	public Integer getDurationInDays() {
		return durationInDays;
	}
	public void setDurationInDays(Integer durationInDays) {
		this.durationInDays = durationInDays;
	}
	public Double getPrice() {
		return price;
	}
	public void setPrice(Double price) {
		this.price = price;
	}
	public Integer getMaxBooksAllowed() {
		return maxBooksAllowed;
	}
	public void setMaxBooksAllowed(Integer maxBooksAllowed) {
		this.maxBooksAllowed = maxBooksAllowed;
	}

    // Getters & Setters
    
}