package com.suman.security.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.suman.security.entities.Contents;
import com.suman.security.entities.Subscription;
import com.suman.security.repository.ContentRepository;
import com.suman.security.repository.SubscriptionRepository;


@RestController
@RequestMapping("/users")
public class UserController {

	@Autowired
	ContentRepository contentRepository;
	
	@Autowired
	SubscriptionRepository subscriptionRepository;
	
	
	@PreAuthorize("hasRole('ADMIN') or hasAuthority('WRITE')")
	@GetMapping("/contents")
	public List<Contents> getAllcontents(){
		return contentRepository.findAll();
	}
	
	@PreAuthorize("hasRole('ADMIN') or hasAuthority('WRITE')")
	@PostMapping("/subscribe")
	public String subscribeContents(@RequestBody Subscription subscription){
		Subscription subc = subscriptionRepository.save(subscription);
		return subc != null ? "Success" : "Failed";
	}
}
