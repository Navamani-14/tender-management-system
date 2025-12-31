package com.example.tenderTest.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.tenderTest.model.UserModel;
import com.example.tenderTest.repository.UserRepository;

@Service
public class UserService {
	
	@Autowired
	private UserRepository userRepository;
	public UserModel findByEmail(String email) {
		return userRepository.findByEmail(email).orElse(null);
	}
	

}
