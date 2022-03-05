package com.suman.security.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.suman.security.entities.Usertoken;

@Repository
public interface UsertokenRepository extends CrudRepository<Usertoken, Long> {

	Usertoken findTokenById(int custId);
}
