package com.suman.security.configuration;

import java.util.Arrays;
import java.util.Collections;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;
import org.springframework.security.web.csrf.CookieCsrfTokenRepository;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;

import com.suman.security.filters.JWTTokenGeneratorFilter;
import com.suman.security.filters.JWTTokenValidatorFilter;
import com.suman.security.repository.CustomerRepository;
import com.suman.security.repository.UsertokenRepository;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true, securedEnabled = true, jsr250Enabled = true)
public class MySecurityConfig extends WebSecurityConfigurerAdapter {

	@Autowired
	CustomerRepository customerRepository;
	
	@Autowired
	UsertokenRepository usertokenRepository;
	
	
	@Override
	protected void configure(HttpSecurity http) throws Exception {
		
		http.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS).and()
		.cors().configurationSource(new CorsConfigurationSource() {
			
			@Override
			public CorsConfiguration getCorsConfiguration(HttpServletRequest request) {
				CorsConfiguration config = new CorsConfiguration();
				config.setAllowedOrigins(Arrays.asList("http://localhost:3000", "192.168.0.7:3000"));
				config.setAllowedMethods(Collections.singletonList("*"));
				config.setAllowCredentials(true);
				config.setAllowedHeaders(Collections.singletonList("*"));
				config.setExposedHeaders(Arrays.asList("Authorization"));
				config.setMaxAge(3600L);
				return config;
			}
		}).and().csrf().disable()
		.addFilterBefore(new JWTTokenValidatorFilter(customerRepository, usertokenRepository), BasicAuthenticationFilter.class)
		.addFilterAfter(new JWTTokenGeneratorFilter(customerRepository, usertokenRepository), BasicAuthenticationFilter.class)
		.authorizeRequests()
		.antMatchers(HttpMethod.POST,"/public/**").permitAll()
		.antMatchers("/user").authenticated()
		.antMatchers("/users/**").authenticated()
		.anyRequest()
		.authenticated()
		.and()
		.httpBasic();
	}

	@Bean
	public PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder(10);
	}
}
