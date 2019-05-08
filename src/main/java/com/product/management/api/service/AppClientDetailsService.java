package com.product.management.api.service;

import org.springframework.security.oauth2.provider.ClientDetailsService;
import org.springframework.security.oauth2.provider.ClientRegistrationService;

public interface AppClientDetailsService extends ClientDetailsService, ClientRegistrationService {
	
}
