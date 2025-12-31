package com.example.tenderTest.service;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Service;

import com.example.tenderTest.model.BiddingModel;
import com.example.tenderTest.model.UserModel;
import com.example.tenderTest.repository.BiddingRepository;
@Service
public class BiddingService {
	@Autowired
	private BiddingRepository biddingRepository;
	@Autowired
	private UserService userService;
	
	private boolean hasRole(Authentication auth,String role) {
		return auth.getAuthorities().stream().map(GrantedAuthority::getAuthority)
				.anyMatch(r->r.equals("ROLE_"+role));
	}
	public ResponseEntity<Object> addBidding(BiddingModel biddingModel, Authentication auth) {
		if(!hasRole(auth,"BIDDER")) {
			return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Only bidders allowed");
		}
		if(biddingModel.getBiddingId()==0 || biddingModel.getBidAmount()==0 || biddingModel.getYearsToComplete()==0) {
			return ResponseEntity.badRequest().body("Invalid data");
		}
		UserModel bidder=userService.findByEmail(auth.getName());
		if(bidder==null) {
			return ResponseEntity.badRequest().body("Invalid Bidder");
		}
		BiddingModel b=new BiddingModel();
		b.setBiddingId(biddingModel.getBiddingId());
		b.setBidAmount(biddingModel.getBidAmount());
		b.setYearsToComplete(biddingModel.getYearsToComplete());
		b.setDateOfBidding(new SimpleDateFormat("dd/MM/yyyy").format(new Date()));
		b.setStatus("pending");
		b.setBidderId(bidder.getId());
		
		BiddingModel saved=new BiddingModel();
		try {
			saved=biddingRepository.save(b);
		}
		catch(Exception e) {
			ResponseEntity.status(HttpStatus.BAD_REQUEST).body("bad request");
		}
		
		return ResponseEntity.status(HttpStatus.CREATED).body(saved);
	}

	public ResponseEntity<Object> listBidding(double bidAmount, Authentication auth) {
		if(bidAmount!=0) {
			List<BiddingModel> list=biddingRepository.findByBidAmountGreaterThan(bidAmount);
			if(list.isEmpty()) {
				return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("no data available");
			}
			return ResponseEntity.ok().body(list);
		}
		else {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Invalid  data");
		}
	}
	public ResponseEntity<Object> partialList(String keyword, Authentication auth) {
		if(keyword!=null) {
		List<BiddingModel> bids= biddingRepository.searchBids(keyword);
		if(bids.isEmpty()) {
			return ResponseEntity.badRequest().body("no data available");
		}
		return ResponseEntity.ok(bids);
	}
		else {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Invalid  data");
		}
	}

	public ResponseEntity<Object> updateBidding(int id, BiddingModel biddingModel, Authentication auth) {
		if(biddingModel.getStatus()!=null) {
			Optional<BiddingModel> opt=biddingRepository.findById(id);
			if(!opt.isPresent()) {
				return ResponseEntity.badRequest().body("No data");
			}
			BiddingModel bi=opt.get();
			bi.setStatus(biddingModel.getStatus());
			biddingRepository.save(bi);
			return ResponseEntity.ok().body(bi);
		}
		return ResponseEntity.badRequest().body("Invalid sttaus");
	}

	public ResponseEntity<Object> deleteMapping(int id, Authentication auth) {
		Optional<BiddingModel> opt=biddingRepository.findById(id);
		if(!opt.isPresent()) {
			return ResponseEntity.badRequest().body("no data available");
		}
		BiddingModel bidding=opt.get();
		boolean isApprover=hasRole(auth,"APPROVER");
		UserModel user=userService.findByEmail(auth.getName());
		if(user==null) {
			return ResponseEntity.badRequest().body("Invalid user");
		}
		Integer userId=user.getId();
		boolean isCreator=bidding.getBidderId()!=0 && (userId==bidding.getBidderId());
		if(!isApprover && !isCreator) {
			return ResponseEntity.status(HttpStatus.FORBIDDEN).body("You don't have permission");
		}
		
		biddingRepository.delete(bidding);
		return ResponseEntity.status(HttpStatus.NO_CONTENT).body("deleted successfully");
		
	}
	public ResponseEntity<Object> addMultipleBidding(List<BiddingModel> bids, Authentication auth) {
	if(!hasRole(auth,"BIDDER")) {
		return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Only bidders allowed");
	}
	UserModel bidder=userService.findByEmail(auth.getName());
	if(bidder==null) {
		return ResponseEntity.badRequest().body("Invalid bidder");
	}
	List<BiddingModel> savedBids=new ArrayList<>();
	for(BiddingModel b:bids) {
		if(b.getBiddingId()==0 || b.getBidAmount()==0 || b.getYearsToComplete()==0) {
			continue;
		}
		BiddingModel newBid=new BiddingModel();
		newBid.setBiddingId(b.getBiddingId());
		newBid.setBidAmount(b.getBidAmount());
		newBid.setYearsToComplete(b.getYearsToComplete());
		newBid.setDateOfBidding(new SimpleDateFormat("dd/MM/yyy").format(new Date()));
		newBid.setStatus("pending");
		newBid.setBidderId(bidder.getId());
		
		try {
			savedBids.add(biddingRepository.save(newBid));
		}
		catch(Exception e) {
			ResponseEntity.status(HttpStatus.BAD_REQUEST).body("bad request");
		}
	}
	
	if(savedBids.isEmpty()) {
		return ResponseEntity.badRequest().body("No valid bids to save");
	}
	return ResponseEntity.status(HttpStatus.CREATED).body(savedBids);
	/* Can use saveAll() also
	     newBid.setDateOfBidding(dateStr);
        newBid.setStatus("pending");
	    newBid.setBidderId(bidder.getId());

        validBids.add(newBid);
    }

    if (validBids.isEmpty()) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("No valid bids to save");
    }

    // Save all valid bids at once
    List<BiddingModel> savedBids = biddingRepository.saveAll(validBids);

    return ResponseEntity.status(HttpStatus.CREATED).body(savedBids);
}
	 * 
	 * 
	 */

}
	

}