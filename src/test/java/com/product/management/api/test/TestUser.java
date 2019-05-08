package com.product.management.api.test;

import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContext;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;
import com.product.management.api.domain.User;
import com.product.management.api.repository.UserRepository;
import java.util.List;
import org.junit.Assert;
import org.junit.Test;;

@RunWith(SpringRunner.class)
@SpringBootTest
@ContextConfiguration(classes = {User.class, UserRepository.class})
@EnableMongoRepositories(basePackages = {"com.product.management.api.repository"})
@EnableAutoConfiguration
public class TestUser{
	
	@Autowired 
	private User user;
	 
	@Autowired
    private ApplicationContext applicationContext;
	
	@Autowired
	private MongoTemplate mongoTemplate;
		
	@Test
	public void test() {
		Assert.assertNotNull("user Oject", user);
		Assert.assertNotNull("context Oject", applicationContext);	
        user.setFirstName("John");
        user.setLastName("Doe");
        user.setEmail("john.doe@example.com");
        mongoTemplate.save(user);
        List<User> users = mongoTemplate.findAll(User.class);
        Assert.assertNotNull(users);
	}
}
