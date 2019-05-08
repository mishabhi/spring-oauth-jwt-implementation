package com.product.management.api.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

@PropertySource(value="classpath:application.properties")
@Configuration
public class AppProperties {
	
	@Value("${spring.application.profiles}")
	private String applicationMode;
	
	public String getApplicationMode() {
		return this.applicationMode;
	}
	
	@Value("${spring.data.mongodb.host}")  
    private String host;
	
	public String getMongoDbHost() {
		return this.host;
	}

    @Value("${spring.data.mongodb.port}")
    private Integer port;
    
    public Integer getMongoDbHostPost() {
		return this.port;
	}
    
    @Value("${spring.data.mongodb.database}")
    private String database;
    
    public String getMongoDbName() {
		return this.database;
	}
    
    @Value("${security.jwt.access.token.expire.length}")
    private int accessTokenExpireTime;
    
    public int getAccessTokenExpireTime() {
    	return this.accessTokenExpireTime;
    }
    @Value("${security.jwt.refresh.token.expire.length}")
    private int refreshTokenExpireTime;
    
    public int getRefreshTokenExpireTime() {
    	return this.refreshTokenExpireTime;
    }
    
    @Value("${security.jwt.token.secret-key}")
    private String tokenSecretKey;
    
    public String getTokenSecretKey() {
    	return this.tokenSecretKey;
    }
}
