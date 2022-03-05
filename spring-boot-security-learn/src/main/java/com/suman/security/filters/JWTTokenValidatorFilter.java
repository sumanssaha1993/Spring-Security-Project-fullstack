package com.suman.security.filters;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.List;

import javax.crypto.SecretKey;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import com.suman.security.constants.SecurityConstants;
import com.suman.security.entities.Customer;
import com.suman.security.repository.CustomerRepository;
import com.suman.security.repository.UsertokenRepository;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;

@Component
public class JWTTokenValidatorFilter extends OncePerRequestFilter {
	
	CustomerRepository customerRepository;
	
	UsertokenRepository usertokenRepository;
	
	public JWTTokenValidatorFilter(CustomerRepository customerRepository, UsertokenRepository usertokenRepository){
		this.customerRepository = customerRepository;
		this.usertokenRepository = usertokenRepository;
	}

	@Override
	public void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
			throws IOException, ServletException {
		String jwt = request.getHeader(SecurityConstants.JWT_HEADER);
		
		if (null != jwt) {
			try {
				
				if(jwt.contains(",")) {
					jwt = jwt.split(",")[0];
				}
				
				List<Customer> customers = customerRepository.findByEmail(request.getHeader("emailid"));
				String softToken = usertokenRepository.findTokenById(customers.get(0).getId()).getToken();
				
				SecretKey key = Keys.hmacShaKeyFor(softToken.getBytes(StandardCharsets.UTF_8));
				
				Claims claims = Jwts.parserBuilder()
						.setSigningKey(key)
						.build()
						.parseClaimsJws(jwt)
						.getBody();
				String username = String.valueOf(claims.get("username"));
				String authorities = (String) claims.get("authorities");
				Authentication auth = new UsernamePasswordAuthenticationToken(username,null,
						AuthorityUtils.commaSeparatedStringToAuthorityList(authorities));
				SecurityContextHolder.getContext().setAuthentication(auth);
			} catch (ExpiredJwtException eje) {
				throw new BadCredentialsException("Token Expired!");
				
			} catch (Exception e) {
				throw new BadCredentialsException("Invalid Token received!");
			}
			
		}
		chain.doFilter(request, response);
	}

	
	@Override 
	protected boolean shouldNotFilter(HttpServletRequest request) {
		  return request.getServletPath().equals("/user"); 
	}

}
