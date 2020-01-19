package com.product.management.api;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.data.mongo.MongoDataAutoConfiguration;
import org.springframework.boot.autoconfigure.mongo.MongoAutoConfiguration;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.context.event.EventListener;
import com.product.management.api.common.Constants;
import com.product.management.api.config.AppProperties;
import com.product.management.api.config.SeedDataService;

@SpringBootApplication(exclude = {MongoAutoConfiguration.class, MongoDataAutoConfiguration.class})
@EnableAutoConfiguration
@ComponentScan(basePackages = {"com.product.management.api"}, useDefaultFilters = true)
public class ApplicationRunner  {
	
	@Autowired
	private ApplicationContext appContext;
  
	public static void main(String[] args) {
		SpringApplication.run(ApplicationRunner.class, args);    
	}
 
	@EventListener
	public void seed(ContextRefreshedEvent event) throws Exception {
		if(appContext.getBean(AppProperties.class).getApplicationMode().equalsIgnoreCase(Constants.ENV_DEVELOPMENT.getValue())){
			appContext.getBean(SeedDataService.class).seedTestData();
		}
	}
}
