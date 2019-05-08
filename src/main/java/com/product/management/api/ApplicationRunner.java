package com.product.management.api;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.data.mongo.MongoDataAutoConfiguration;
import org.springframework.boot.autoconfigure.mongo.MongoAutoConfiguration;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.ComponentScan;
import com.product.management.api.common.Constants;
import com.product.management.api.config.AppProperties;
import com.product.management.api.config.SeedDataService;

@SpringBootApplication(exclude = {MongoAutoConfiguration.class, MongoDataAutoConfiguration.class})
@ComponentScan("com.product.management.api")
@EnableAutoConfiguration
public class ApplicationRunner implements CommandLineRunner {
	
	@Autowired
	private ApplicationContext appContext;
  
	public static void main(String[] args) {
		SpringApplication.run(ApplicationRunner.class, args);    
	}
 
	@Override
	public void run(String... appMode) throws Exception {
		if(appContext.getBean(AppProperties.class).getApplicationMode().equalsIgnoreCase(Constants.ENV_DEVELOPMENT.getValue())){
			appContext.getBean(SeedDataService.class).seedTestData();
		}
	}
}
