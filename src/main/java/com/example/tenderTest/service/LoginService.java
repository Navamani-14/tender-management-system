package com.example.tenderTest.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.example.tenderTest.model.UserModel;

@Service
public class LoginService implements UserDetailsService{
	
	@Autowired
	private UserService userService;
	
	public UserDetails loadUserByUsername(String email)throws UsernameNotFoundException{
		UserModel user=userService.findByEmail(email);
		if(user==null) {
			throw new UsernameNotFoundException("username not found: "+email);
			
		}
		
		List<GrantedAuthority> authorities=new ArrayList<>();
		authorities.add(new SimpleGrantedAuthority("ROLE_"+user.getRole().getRolename().toUpperCase()));
		
		return org.springframework.security.core.userdetails.User
				.withUsername(user.getEmail())
				.password(user.getPassword())
				.authorities(authorities)
				.accountExpired(false)
				.accountLocked(false)
				.credentialsExpired(false)
				.disabled(false)
				.build();
	}
	
	private UserDetails buildUserForAuthentication(UserModel user,List<GrantedAuthority> authorities) {
		return null;
	}
	private List<GrantedAuthority> builduserAuthority(String userRole){
		return null;
	}
	
	

}
