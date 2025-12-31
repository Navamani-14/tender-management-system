package com.example.tenderTest.security;

import java.nio.charset.StandardCharsets;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

import javax.crypto.SecretKey;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;

@Service
public class JwtService {
	
	@Value("${jwt.secret}")
	private String SECRET;
    private final long JWT_TOKEN_VALIDITY = 9000000; 
    
    private SecretKey signingKey() {
    	return Keys.hmacShaKeyFor(SECRET.getBytes(StandardCharsets.UTF_8));
    }
    
    public String generateToken(UserDetails userDetails) {
    	Map<String,Object> claims=new HashMap<>();
    	List<String>roles=userDetails.getAuthorities().stream().map(GrantedAuthority::getAuthority)
    			.collect(Collectors.toList());
    	claims.put("roles",roles);
    	return createToken(claims,userDetails.getUsername());
    }
    private String createToken(Map<String,Object> claims,String subject) {
    	long now=System.currentTimeMillis();
    	return Jwts.builder()
    			.setClaims(claims)
    			.setSubject(subject)
    			.setIssuedAt(new Date(now))
    			.setExpiration(new Date(now+JWT_TOKEN_VALIDITY))
    		    .signWith(signingKey(),SignatureAlgorithm.HS512)
    		    .compact();
    }
    private Claims extractAllClaims(String token) {
    	return Jwts.parserBuilder()
    			.setSigningKey(signingKey())
    			.build()
    			.parseClaimsJws(token)
    			.getBody();
    }
    
    public <T> T extractClaim(String token,Function<Claims,T> func) {
    	return func.apply(extractAllClaims(token));
    }
    
    public String extractUsername(String token) {
    	return extractClaim(token,Claims::getSubject);
    }
    @SuppressWarnings("unchecked")
    public List<String> extractRoles(String token){
    	Object obj=extractClaim(token,c->c.get("roles"));
    	if(obj instanceof List) return (List<String>) obj;
    	return Collections.emptyList();
    	}
    
    public boolean isTokenExpired(String token) {
    	Date exp=extractClaim(token,Claims::getExpiration);
    	return exp.before(new Date());
    }
    
    public boolean validateToken(String token,UserDetails userDetails) {
    	final String username=extractUsername(token);
    	return username.equals(userDetails.getUsername()) && !isTokenExpired(token);
    }


}
