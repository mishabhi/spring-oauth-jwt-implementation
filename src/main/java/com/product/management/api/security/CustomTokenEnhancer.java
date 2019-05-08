package com.product.management.api.security;

import java.util.HashMap;
import java.util.Map;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.oauth2.common.DefaultOAuth2AccessToken;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.security.oauth2.provider.token.store.JwtAccessTokenConverter;


public class CustomTokenEnhancer extends JwtAccessTokenConverter {
  @Override
  public OAuth2AccessToken enhance(OAuth2AccessToken accessToken, OAuth2Authentication authentication) {
      if(authentication.getOAuth2Request().getGrantType().equalsIgnoreCase("password")) {
    	  final Map<String, Object> additionalInfo = new HashMap<String, Object>();
    	  
          UserDetails clientDetails = (UserDetails)authentication.getPrincipal();
          additionalInfo.put("clientId", clientDetails.getUsername());
          
    	  User userDetails = (User) authentication.getUserAuthentication().getPrincipal();
    	  
    	  if(userDetails instanceof User) {
    		  additionalInfo.put("username", userDetails.getUsername());
    	  }
          ((DefaultOAuth2AccessToken) accessToken).setAdditionalInformation(additionalInfo);    
      } 
      accessToken = super.enhance(accessToken, authentication);
      ((DefaultOAuth2AccessToken) accessToken).setAdditionalInformation(new HashMap<>());
      return accessToken;
  }
}
