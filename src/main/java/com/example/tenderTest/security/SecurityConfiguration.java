package com.example.tenderTest.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.example.tenderTest.service.LoginService;

@Configuration
@EnableMethodSecurity
public class SecurityConfiguration {
	
	@Autowired
	private AuthEntry authEntry;
	@Autowired
	private AccessDenied accessDenied;
	@Autowired
	private LoginService loginService;
	@Autowired
	private AuthenticationFilter authenticationFilter;
	
	@Bean
	public BCryptPasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}
	
	  @Bean
	    public AuthenticationProvider authenticationProvider() {
	        DaoAuthenticationProvider p = new DaoAuthenticationProvider();
	        p.setUserDetailsService(loginService);
	        p.setPasswordEncoder(passwordEncoder()); 
	        //p.setPasswordEncoder(new BCryptPasswordEncoder(10))
	        return p;
	    }
	  @Bean
	  public AuthenticationManager authenticationManager(AuthenticationConfiguration config)throws Exception{
		  return config.getAuthenticationManager();
	  }
	  @Bean
	  public SecurityFilterChain filterChain(HttpSecurity http) throws Exception{
		  http.
		  csrf(csrf->csrf.disable())
		  .sessionManagement(sess->sess.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
		  .authenticationProvider(authenticationProvider())
		  .headers(headers->headers.frameOptions().disable())
		  .exceptionHandling(exception->exception.authenticationEntryPoint(authEntry)
				  .accessDeniedHandler(accessDenied))
		  .addFilterBefore(authenticationFilter,UsernamePasswordAuthenticationFilter.class)
		  .authorizeHttpRequests(auth->auth
				  .requestMatchers("/login").permitAll()
				  .requestMatchers("/h2-console/**").permitAll()
				  .requestMatchers("/v3/api-docs/**","/swagger-ui/**","swagger-ui.html").permitAll()
				  .anyRequest().authenticated()
				  );
		  return http.build();
		  
	  }

}
