package com.example.tenderTest.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;

@Entity
public class BiddingModel {
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private Integer id;
	@Column(unique=true)
	private int biddingId;
	private final String projectName = "Metro Phase V 2024";
	private Double bidAmount;
	private Double yearsToComplete;
	private String dateOfBidding;
	private String status;
	
	private Integer bidderId;
	public BiddingModel(Integer id, int biddingId, Double bidAmount, Double yearsToComplete, String dateOfBidding,
			String status, Integer bidderId) {
		super();
		this.id = id;
		this.biddingId = biddingId;
		this.bidAmount = bidAmount;
		this.yearsToComplete = yearsToComplete;
		this.dateOfBidding = dateOfBidding;
		this.status = status;
		this.bidderId = bidderId;
	} 
	public BiddingModel() {
		
	}
	public BiddingModel(String status) {
		super();
		this.status = status;
	}
	public BiddingModel(int biddingId, Double bidAmount, Double yearsToComplete) {
		super();
		this.biddingId = biddingId;
		this.bidAmount = bidAmount;
		this.yearsToComplete = yearsToComplete;
	}
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public int getBiddingId() {
		return biddingId;
	}
	public void setBiddingId(int biddingId) {
		this.biddingId = biddingId;
	}
	public Double getBidAmount() {
		return bidAmount;
	}
	public void setBidAmount(Double bidAmount) {
		this.bidAmount = bidAmount;
	}
	public Double getYearsToComplete() {
		return yearsToComplete;
	}
	public void setYearsToComplete(Double yearsToComplete) {
		this.yearsToComplete = yearsToComplete;
	}
	public String getDateOfBidding() {
		return dateOfBidding;
	}
	public void setDateOfBidding(String dateOfBidding) {
		this.dateOfBidding = dateOfBidding;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public Integer getBidderId() {
		return bidderId;
	}
	public void setBidderId(Integer bidderId) {
		this.bidderId = bidderId;
	}
	public String getProjectName() {
		return projectName;
	}
	
	

}
