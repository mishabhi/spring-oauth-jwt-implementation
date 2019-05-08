package com.product.management.api.config;


import java.util.Arrays;
import org.slf4j.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.stereotype.Component;
import com.google.common.collect.Sets;
import com.product.management.api.domain.Role;
import com.product.management.api.domain.User;
import com.product.management.api.model.AccessToken;
import com.product.management.api.model.AuthorizationCode;
import com.product.management.api.model.CustomClientDetails;

@Component
public class SeedDataService {
	
	private static Logger LOGGER = LoggerFactory.getLogger(SeedDataService.class);
		
	@Autowired private MongoTemplate mongoTemplate;
	
	@Autowired private CustomClientDetails clientDetails;
	  
	public void seedTestData() {
		
		LOGGER.info("************Test Data Seeding Start*********");
		//MongoTemplate mongoTemplate = context.getBean(MongoTemplate.class);

        mongoTemplate.dropCollection(User.class);
        mongoTemplate.dropCollection(CustomClientDetails.class);
        mongoTemplate.dropCollection(AccessToken.class);
        mongoTemplate.dropCollection(AuthorizationCode.class);

        // add user
        User mongoUser = new User().setUsername("admin.user")
        		 				   //.setPassword(passwordEncoder.encode("Password@123"))
        						   .setPassword("Password@123")
        		 				   .setFirstName("Test")
        		 				   .setLastName("User")
        		 				   .setEmail("software.geekcoder@gmail.com")
        		 				   .setRoles(Arrays.asList(Role.ROLE_USER.getAuthority(), Role.ROLE_ADMIN.getAuthority()));
        mongoTemplate.save(mongoUser);

        // add oauth client details
        clientDetails.setClientId("dev-client");
        clientDetails.setClientSecret("client-secret");
        clientDetails.setSecretRequired(true);
        clientDetails.setResourceIds(Sets.newHashSet("app-resource-1", "app-resource-2"));
        clientDetails.setScope(Sets.newHashSet("write"));
        clientDetails.setAuthorizedGrantTypes(Sets.newHashSet("authorization_code", "refresh_token", "password", "client_credentials"));
        clientDetails.setRegisteredRedirectUri(Sets.newHashSet("http://localhost:8080/product-management/api"));
        clientDetails.setAuthorities(AuthorityUtils.createAuthorityList("ROLE_USER"));
        clientDetails.setAutoApprove(false);
        mongoTemplate.save(clientDetails);
        LOGGER.info("************Test Data Seeding End***********");
	}
}
