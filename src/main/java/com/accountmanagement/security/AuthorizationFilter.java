package com.accountmanagement.security;

import com.accountmanagement.dto.JWTPayload;
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
	public static final String KEY_AUTHORIZATION = "authorization";
	
	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
			throws IOException, ServletException {
    	try {
			JWTPayload jwtPayload = jwtService.getJWTPayload(request.getHeader(KEY_AUTHORIZATION));
			SecurityContextHolder.getContext().setAuthentication(new UsernamePasswordAuthenticationToken(jwtPayload.id(), null,  List.of(jwtPayload.role(), jwtPayload.tokenType())));
        	chain.doFilter(request, response);
		} catch (Exception e) {
	    	chain.doFilter(request, response);
		}
	}

}
