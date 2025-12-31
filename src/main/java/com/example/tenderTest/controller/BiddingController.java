package com.example.tenderTest.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.tenderTest.model.BiddingModel;
import com.example.tenderTest.service.BiddingService;

@RestController
@RequestMapping("/bidding")
public class BiddingController {
	@Autowired
	private BiddingService biddingService;
	
	@PostMapping("/add")
	@PreAuthorize("hasRole('BIDDER')")
	public ResponseEntity<Object> addBidding(@RequestBody BiddingModel biddingModel,Authentication auth){
		return biddingService.addBidding(biddingModel,auth);
	}
	@PostMapping("/addMulitiple")
	@PreAuthorize("hasRole('BIDDER')")
	public ResponseEntity<Object> addMultipleBidding(@RequestBody List<BiddingModel> biddingModel,Authentication auth){
		return biddingService.addMultipleBidding(biddingModel,auth);
	}

	@GetMapping("/list")
	@PreAuthorize("hasAnyRole('BIDDER','APPROVER')")
	public ResponseEntity<Object> listBidding(@RequestParam(name="bidAmount",required=true) double bidAmount,Authentication auth){
		return biddingService.listBidding(bidAmount,auth);
	}
	
	@GetMapping("/partialList")
	@PreAuthorize("hasAnyRole('BIDDER','APPROVER')")
	public ResponseEntity<Object> partialList(@RequestParam String keyword,Authentication auth){
		return biddingService.partialList(keyword,auth);
	}
	@PatchMapping("/update/{id}")
	@PreAuthorize("hasRole('APPROVER')")
	public ResponseEntity<Object> updateBidding(@PathVariable("id") int id,@RequestBody BiddingModel biddingModel,Authentication auth){
		return biddingService.updateBidding(id,biddingModel,auth);
	}
	
	@DeleteMapping("/delete/{id}")
	@PreAuthorize("hasAnyRole('BIDDER','APPROVER')")
	public ResponseEntity<Object> deleteBidding(@PathVariable("id") int id,Authentication auth){
		return biddingService.deleteMapping(id,auth);
	}

}
