package com.example.tenderTest.security;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import com.example.tenderTest.service.LoginService;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class AuthenticationFilter extends OncePerRequestFilter{
	
	@Autowired
	private JwtService jwtService;
	@Autowired
	private LoginService loginService; 
	
	@Override
	protected void doFilterInternal(HttpServletRequest request,HttpServletResponse response,FilterChain chain)
	throws IOException,ServletException{
		final String auth=request.getHeader("Authorization");
		if(auth==null || !auth.startsWith("Bearer ")) {
			chain.doFilter(request, response);
			return;
		}
		final String token=auth.substring(7);
		final String username;
		try {
			username=jwtService.extractUsername(token);
		}
		catch(Exception e) {
			chain.doFilter(request, response);
			return;
			
		}
		
		if(username!=null && SecurityContextHolder.getContext().getAuthentication()==null) {
			UserDetails userDetails=loginService.loadUserByUsername(username);
			if(jwtService.validateToken(token, userDetails)) {
				UsernamePasswordAuthenticationToken authToken=new UsernamePasswordAuthenticationToken
						(userDetails,null,userDetails.getAuthorities());
				authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
				SecurityContextHolder.getContext().setAuthentication(authToken);
			}
		}
		chain.doFilter(request, response);
	}
	
}
