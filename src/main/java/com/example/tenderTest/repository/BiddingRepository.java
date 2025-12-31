 package com.example.tenderTest.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.example.tenderTest.model.BiddingModel;

@Repository
public interface BiddingRepository extends JpaRepository<BiddingModel,Integer>{

	List<BiddingModel> findByBidAmountGreaterThan(Double bidAmount);

	@Query("select b from BiddingModel b where " +
	    "LOWER(b.projectName) LIKE LOWER(CONCAT('%', :keyword ,'%')) OR " +
			"LOWER(b.status) LIKE LOWER(CONCAT('%', :keyword , '%'))")
	List<BiddingModel> searchBids(@Param("keyword") String keyword);
	
	/*
	 * 
	 @Query("select p from Product p where "+
	 "LOWER(p.name) LIKE LOWER(CONCAT('%', :key ,'%')) "+
	 AND p.age=:age)
	 List<BiddingModel> list(@Param("key") String key,@Param("age") int age);
	 * 
	 */
}
