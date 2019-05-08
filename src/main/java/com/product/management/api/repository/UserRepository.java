package com.product.management.api.repository;


import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.transaction.annotation.Transactional;
import com.product.management.api.domain.User;

public interface UserRepository extends MongoRepository<User, String> {

	public User findByUsername(String username);

	@Transactional 
	public boolean deleteByUsername(String username);
}
