package com.example.tenderTest.controller;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.example.tenderTest.dto.LoginDto;
import com.example.tenderTest.security.JwtService;
import com.example.tenderTest.service.LoginService;

@RestController
public class LoginController {
	@Autowired
	private AuthenticationManager authenticationManager;
	@Autowired
	private JwtService jwtService;
	@Autowired
	private LoginService loginService;
	
	@PostMapping("/login")
	public ResponseEntity<Map<String,Object>> login(@RequestBody LoginDto loginDto) throws Exception{
		try {
			authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(loginDto.getEmail(),loginDto.getPassword()));
		}
		catch(BadCredentialsException e) {
			Map<String,Object> map=new HashMap<>();
			map.put("message", "Invalid credentilas");
			return ResponseEntity.badRequest().body(map);
		}
		UserDetails userDetails=loginService.loadUserByUsername(loginDto.getEmail());
		String token=jwtService.generateToken(userDetails);
		Map<String,Object> resp=new HashMap<>();
		resp.put("jwt", token);
		resp.put("status", Integer.valueOf(200));
		return ResponseEntity.ok(resp);
	}

}
