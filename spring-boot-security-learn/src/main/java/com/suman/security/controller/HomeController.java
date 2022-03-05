package com.suman.security.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.suman.security.authproviders.AuthenticationProviderCustom;
import com.suman.security.entities.Customer;

@RestController
@RequestMapping("/public")
public class HomeController {
	
	@Autowired
	AuthenticationProviderCustom authenticationProviderCustom;
	
	@GetMapping("/home")
	public String home() {
		
		return "Free access";
	}

	@PostMapping("/add")
	public String addUser(@RequestBody Customer cust) {
		
		return authenticationProviderCustom.addCustomer(cust);
	}
}
