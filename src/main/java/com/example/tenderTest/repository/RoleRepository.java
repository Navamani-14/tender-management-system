package com.example.tenderTest.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.tenderTest.model.RoleModel;

@Repository
public interface RoleRepository extends JpaRepository<RoleModel,Integer>{
	
	Optional<RoleModel> findByRolename(String rolename);
	

}
