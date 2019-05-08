package com.product.management.api.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.context.support.PropertySourcesPlaceholderConfigurer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.provider.ClientDetailsService;
import org.springframework.security.oauth2.provider.code.AuthorizationCodeServices;
import org.springframework.security.oauth2.provider.token.AuthenticationKeyGenerator;
import org.springframework.security.oauth2.provider.token.DefaultAuthenticationKeyGenerator;
import org.springframework.security.oauth2.provider.token.DefaultTokenServices;
import org.springframework.security.oauth2.provider.token.TokenEnhancer;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.security.oauth2.provider.token.store.JwtAccessTokenConverter;
import org.springframework.security.web.AuthenticationEntryPoint;
import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
import com.product.management.api.model.CustomApiResponse;
import com.product.management.api.security.CustomTokenEnhancer;
import com.product.management.api.security.CustomAuthenticationEntryPoint;
import com.product.management.api.service.impl.CustomAuthorizationCodeServices;
import com.product.management.api.model.CustomClientDetails;
import com.product.management.api.service.impl.CustomClientDetailsService;
import com.product.management.api.security.CustomTokenStore;

@Configuration
public class BeanFactory {
	
	@Bean
 	public AppProperties appProperties() {
 		return new AppProperties();
 	}
	 
 	@Bean
 	public CustomApiResponse apiResponse() {
 		return new CustomApiResponse();
 	}	
 	
 	@Bean
 	public CustomClientDetails customClientDetails() {
		 return new CustomClientDetails();
	}
 	 
 	@Bean
 	public  AuthenticationEntryPoint getAuthenticationEntryPoint(){
 		return new CustomAuthenticationEntryPoint();
 	}	

 	 
 	@Bean
 	public PasswordEncoder passwordEncoder() {
 		return new BCryptPasswordEncoder(12);
 	}
 
 	@Bean
 	public PropertySourcesPlaceholderConfigurer getConfiProperties() {
 		return new PropertySourcesPlaceholderConfigurer();
 	}
 
 	@Bean
 	public DBObject getMongoDatabaseObject() {
 		return new BasicDBObject();
 	}
	 
 	@Bean
 	public AuthorizationCodeServices authorizationCodeServices() {
 		return new CustomAuthorizationCodeServices();
 	}
	 
 	@Bean(name = "tokenService")
 	@Primary
 	public DefaultTokenServices tokenServices() {
 		final DefaultTokenServices defaultTokenServices = new DefaultTokenServices();
 		defaultTokenServices.setTokenStore(this.tokenStore());
 		defaultTokenServices.setSupportRefreshToken(true);
 		defaultTokenServices.setClientDetailsService(this.clientDetailsService());
 		defaultTokenServices.setTokenEnhancer(accessTokenConverter());
 		defaultTokenServices.setAccessTokenValiditySeconds(this.appProperties().getAccessTokenExpireTime());
 		defaultTokenServices.setRefreshTokenValiditySeconds(this.appProperties().getRefreshTokenExpireTime());
 		return defaultTokenServices;
 	}
	 
 	@Bean
 	public TokenStore tokenStore() {
 		return new CustomTokenStore(accessTokenConverter());
 	}

 	@Bean
 	public AuthenticationKeyGenerator authKeyGenerator() {
 		return new DefaultAuthenticationKeyGenerator();
 	}
	 
 	@Bean
 	public JwtAccessTokenConverter accessTokenConverter() {
 		CustomTokenEnhancer tokenConverter = new CustomTokenEnhancer();
 		tokenConverter.setSigningKey(this.appProperties().getTokenSecretKey());
 		return tokenConverter;
 	}
	     
 	@Bean
 	public TokenEnhancer tokenEnhancer() {
 		return new CustomTokenEnhancer();
 	}
	 
 	@Bean(name = "customClientDetailsService")
 	@Primary
 	public ClientDetailsService clientDetailsService() {
 		return new CustomClientDetailsService();
 	}
}
