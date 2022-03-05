package com.suman.security.authproviders;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import com.suman.security.entities.Authority;
import com.suman.security.entities.Customer;
import com.suman.security.repository.CustomerRepository;

@Component
public class AuthenticationProviderCustom implements AuthenticationProvider {
	
	@Autowired
	CustomerRepository customerRepository;

	@Autowired
	PasswordEncoder passwordEncoder;
	
	@Override
	public Authentication authenticate(Authentication authentication) throws AuthenticationException {
		String username = authentication.getName();
		String password = authentication.getCredentials().toString();
		List<Customer> customer = customerRepository.findByEmail(username);
		if(customer.size() > 0) {
			if(passwordEncoder.matches(password, customer.get(0).getPwd())) {
				List<GrantedAuthority> authorities = getAuthoritiesForUser(customer.get(0));
				return new UsernamePasswordAuthenticationToken(username, password, authorities);
			}
			else {
				throw new BadCredentialsException("Invalid password!");
			}
		}
		else {
			throw new BadCredentialsException("No user registered with this details!");
		}
	}
	
	private List<GrantedAuthority> getAuthoritiesForUser(Customer customer){
		
		List<GrantedAuthority> authorities = new ArrayList<GrantedAuthority>();
		
		for(Authority authority : customer.getAuthorities()) {
			authorities.add(new SimpleGrantedAuthority(authority.getName()));
		}
		
		return authorities;
	}

	@Override
	public boolean supports(Class<?> authenticationType) {
		return authenticationType.equals(UsernamePasswordAuthenticationToken.class);
	}
	
	public String addCustomer(Customer cust) {
		cust.setPwd(passwordEncoder.encode(cust.getPwd()));
		Authority authority1 = new Authority();
		authority1.setCustomer(cust);
		authority1.setName("WRITE");
		
		Authority authority2 = new Authority();
		authority2.setCustomer(cust);
		authority2.setName("READ");
		
		Set<Authority> authorities = new HashSet<Authority>();
		authorities.add(authority1);
		authorities.add(authority2);
		
		cust.setAuthorities(authorities);
		
		Customer savedCust = customerRepository.save(cust);
		if(savedCust != null) {
			return "success";
		}
		else {
			return "Failed";
		}
	}

}
