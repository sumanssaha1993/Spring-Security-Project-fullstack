package com.suman.security.filters;

import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.sql.Timestamp;
import java.util.Collection;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.Set;

import javax.crypto.SecretKey;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import com.suman.security.constants.SecurityConstants;
import com.suman.security.entities.Customer;
import com.suman.security.entities.Usertoken;
import com.suman.security.repository.CustomerRepository;
import com.suman.security.repository.UsertokenRepository;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;

@Component
public class JWTTokenGeneratorFilter extends OncePerRequestFilter {
	
	CustomerRepository customerRepository;
	
	UsertokenRepository usertokenRepository;
	
	public JWTTokenGeneratorFilter(CustomerRepository customerRepository, UsertokenRepository usertokenRepository){
		this.customerRepository = customerRepository;
		this.usertokenRepository = usertokenRepository;
	}

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
			throws ServletException, IOException {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		
		if (null != authentication) {
			
			List<Customer> customers = customerRepository.findByEmail(authentication.getName());
			
			Usertoken usertokendetails = usertokenRepository.findTokenById(customers.get(0).getId());
			
			String softToken = "";
			
			if(usertokendetails != null) {
				softToken = usertokendetails.getToken();
			}
			else {
				softToken = generateRandomToken(100);
				Usertoken  usertoken = new Usertoken();
				usertoken.setId(customers.get(0).getId());
				usertoken.setToken(softToken);
				usertoken.setCreated_on(new Timestamp(new Date().getTime()));
				usertoken.setIs_valid(true);
				usertokenRepository.save(usertoken);
			}
			
			SecretKey key = Keys.hmacShaKeyFor(softToken.getBytes(StandardCharsets.UTF_8));
			
			String jwt = Jwts.builder().setIssuer("Suman Saha").setSubject("JWT Token")
						.claim("username", authentication.getName())
					  .claim("authorities", populateAuthorities(authentication.getAuthorities()))
					  .setIssuedAt(new Date())
					.setExpiration(new Date((new Date()).getTime() + 30000))
					.signWith(key).compact();
			response.setHeader(SecurityConstants.JWT_HEADER, jwt);
		}

		filterChain.doFilter(request, response);
	}
	
	
	private String generateRandomToken(int n) {
		byte[] array = new byte[256];
        new Random().nextBytes(array);
  
        String randomString
            = new String(array, Charset.forName("UTF-8"));
  
        StringBuffer r = new StringBuffer();
  
        for (int k = 0; k < randomString.length(); k++) {
  
            char ch = randomString.charAt(k);
  
            if (((ch >= 'a' && ch <= 'z')
                 || (ch >= 'A' && ch <= 'Z')
                 || (ch >= '0' && ch <= '9'))
                && (n > 0)) {
  
                r.append(ch);
                n--;
            }
        }
  
        return r.toString().substring(0, 32);
	}


	private Object populateAuthorities(Collection<? extends GrantedAuthority> authorities) {
		Set<String> authoritiesSet = new HashSet<>();
        for (GrantedAuthority authority : authorities) {
        	authoritiesSet.add(authority.getAuthority());
        }
        return String.join(",", authoritiesSet);
	}


	@Override
	protected boolean shouldNotFilter(HttpServletRequest request) {
		return !request.getServletPath().equals("/user");
	}

}
