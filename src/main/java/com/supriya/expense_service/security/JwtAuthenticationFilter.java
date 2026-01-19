package com.supriya.expense_service.security;

import java.util.List;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

	private final JwtUtil jwtUtil;
	
	public JwtAuthenticationFilter(JwtUtil jwtUtil) {
		
		this.jwtUtil = jwtUtil;
	}
	

	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, java.io.IOException {
		
	String authHeader = request.getHeader("Authorization");
	
	if(authHeader != null && authHeader.startsWith("Bearer ")) {
		
		String token = authHeader.substring(7);
		
		if(jwtUtil.validateToken(token)) {
			
			String email = jwtUtil.extractEmail(token);
			String role = jwtUtil.extractRole(token);
			
			UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(email, null, List.of(new SimpleGrantedAuthority(role)));
			
			SecurityContextHolder.getContext().setAuthentication(authentication);
		}
	}
	
	filterChain.doFilter(request, response);
	}
}
