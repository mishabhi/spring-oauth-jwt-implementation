package com.product.management.api.domain;

import org.springframework.security.core.GrantedAuthority;

public enum Role implements GrantedAuthority {
	  ROLE_ADMIN, ROLE_GUEST, ROLE_USER;
		
	  @Override
	  public String getAuthority() {
	    return super.name();
	  }
}
