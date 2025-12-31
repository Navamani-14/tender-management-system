package com.example.tenderTest.security;

import java.io.IOException;

import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class AuthEntry implements AuthenticationEntryPoint{
	
	@Override
	public void commence(HttpServletRequest request,HttpServletResponse response,
			AuthenticationException authenticationException)throws IOException,ServletException{
		response.sendError(401);
	}
	

}
