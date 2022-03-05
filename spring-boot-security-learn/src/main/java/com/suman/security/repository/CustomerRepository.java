package com.suman.security.repository;

import java.util.List;

import org.springframework.data.repository.CrudRepository;

import com.suman.security.entities.Customer;

public interface CustomerRepository extends CrudRepository<Customer, Long>{

	List<Customer> findByEmail(String email);
}
