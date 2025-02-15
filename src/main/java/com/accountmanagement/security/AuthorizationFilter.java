package com.accountmanagement.security;

import com.accountmanagement.service.JwtService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.List;

@Component
@RequiredArgsConstructor
public class AuthorizationFilter extends OncePerRequestFilter {
	private final JwtService jwtService;

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
			throws IOException, ServletException {
    	try {
			JwtService.JWTPayload jwtPayload = jwtService.getJWTPayload(request.getHeader(JwtService.AUTHORIZATION_HEADER));
			SecurityContextHolder.getContext().setAuthentication(new UsernamePasswordAuthenticationToken(jwtPayload.userId(), null,  List.of(jwtPayload.role(), jwtPayload.tokenType())));
        	chain.doFilter(request, response);
		} catch (Exception e) {
	    	chain.doFilter(request, response);
		}
	}

}
