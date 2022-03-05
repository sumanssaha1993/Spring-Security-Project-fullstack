package com.suman.security.repository;

import java.util.List;

import org.springframework.data.repository.CrudRepository;

import com.suman.security.entities.Contents;

public interface ContentRepository extends CrudRepository<Contents, Long> {
	List<Contents> findAll();
}
