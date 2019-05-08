package com.product.management.api.common;

public enum Constants {
	
	TOKEN_ID("tokenId"), REFRESH_TOKEN("refreshToken"), AUTHENTICATION_ID("authenticationId"),
			
	ACCESS_TOKEN_STARTS_WITH("Bearer"), ACCESS_TOKEN_HEADER("Authorization"), AUTHORIZATION_CODE("code"),
	
	CLIENT_ID("clientId"), CLIENT_SECRET("clientSecret"), USERNAME("username"),
	
	RESOURCE_IDS("resourceIds"), SCOPE("scope"),
	
	REGISTERED_REDIRECT_URI("registeredRedirectUri"), AUTHORITIES("authorities"), AUTHORIZED_GRANT_TYPES("authorizedGrantTypes"),
	
	ACCESS_TOKEN_VALIDITY_SECONDS("accessTokenValiditySeconds"), REFRESH_TOKEN_VALIDITY_SECONDS("refreshTokenValiditySeconds"), ADDITIONAL_INFORMATION("additionalInformation"),
	
	ENV_TEST("test"), ENV_PRODUCTION("production"), ENV_DEVELOPMENT("development");
	
	private final String enumKeyValue; 
	
	private Constants(String enumKeyValue) {
		this.enumKeyValue = enumKeyValue;
	}
	
	public String getValue() {
		return this.enumKeyValue;
	}
}
