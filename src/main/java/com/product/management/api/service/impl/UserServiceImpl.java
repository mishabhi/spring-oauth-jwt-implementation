package com.product.management.api.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.security.oauth2.provider.token.DefaultTokenServices;
import org.springframework.stereotype.Service;
import com.product.management.api.common.Constants;
import com.product.management.api.common.Messages;
import com.product.management.api.domain.User;
import com.product.management.api.exception.CustomException;
import com.product.management.api.model.CustomApiResponse;
import com.product.management.api.repository.UserRepository;
import com.product.management.api.service.UserService;
import java.util.HashMap;
import javax.servlet.http.HttpServletRequest;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Service
public class UserServiceImpl implements UserService {
	
  private static final Logger LOGGER = LoggerFactory.getLogger(UserService.class);
	
  @Autowired private UserRepository userRepository;

  @Autowired private PasswordEncoder passwordEncoder;
 
  @Autowired @Qualifier(value = "tokenService") private DefaultTokenServices tokenService;
  
  public CustomApiResponse signup(User user) throws CustomException {
	if(user==null) {
		LOGGER.error("Error while signing up as user object is null.");
		throw new CustomException(StringUtils.trim(Messages.PROPERTIES.getPropertyValue("user.signup.error")), HttpStatus.BAD_REQUEST);
	}
	
	if (this.userRepository.findByUsername(user.getUsername())!=null){
    	LOGGER.error("User " + user.getUsername() + " already exists.");
    	throw new CustomException(StringUtils.trim(user.getUsername() + " " + 
    						Messages.PROPERTIES.getPropertyValue("user.signup.create.username.not.available")) , HttpStatus.BAD_REQUEST);
    } 
    try {
	    user.setPassword(this.passwordEncoder.encode(user.getPassword()));
	    user = this.userRepository.save(user);
	    LOGGER.info(StringUtils.trim(Messages.PROPERTIES.getPropertyValue("user.signup.create.success")) + ":" + user.getUsername() + ".");
	    return new CustomApiResponse().setMessage(StringUtils.trim(Messages.PROPERTIES.getPropertyValue("user.signup.create.success")) + ":" + user.getUsername())
	    						.setStatusCode(HttpStatus.CREATED);      
    } catch(Exception expObj) {
		LOGGER.error("Exception occured while creating user with " + user.getUsername() + ".Error:" + expObj);
		throw new CustomException(StringUtils.trim(Messages.PROPERTIES.getPropertyValue("user.signup.error")) + ": " + user.getUsername(), HttpStatus.INTERNAL_SERVER_ERROR);
    }
  }

  public CustomApiResponse deleteUser(String username) throws CustomException {
	  try {	
			if(this.userRepository.findByUsername(username)!=null) {
	            this.userRepository.delete(username);
				LOGGER.info("User " + username + " successfully deleted.");
				return new CustomApiResponse().setMessage(Messages.PROPERTIES.getPropertyValue("user.delete.success"))
										.setStatusCode(HttpStatus.OK)
										.setCustomResponse(new HashMap<String, Boolean>().put("deletionStatus", true).getClass());
			} else{
	            LOGGER.info("User " + username + " does not exist.");
	            return new CustomApiResponse().setMessage(Messages.PROPERTIES.getPropertyValue("user.delete.success"))
						.setStatusCode(HttpStatus.OK)
						.setCustomResponse(new HashMap<String, Boolean>().put("deletionStatus", false).getClass());
	        }
	  }catch(Exception expObj) {
			LOGGER.error("Exception occured while attempting to delete user:  " + username + ". Exception:" + expObj);
			throw new CustomException(StringUtils.trim(Messages.PROPERTIES.getPropertyValue("user.delete.error")) + ":" + username, HttpStatus.INTERNAL_SERVER_ERROR);
	  }  
  }

  public User searchUser(String username) throws CustomException {
	  User userDetails = this.userRepository.findByUsername(username);
	  if(userDetails==null) {
		  LOGGER.info("User " + username + " does not exist.");	
		  throw new CustomException(StringUtils.trim(Messages.PROPERTIES.getPropertyValue("user.search.error")) + ":" + username, HttpStatus.NOT_FOUND);
	  }
	  return userDetails.setPassword(null);
  }
  
  public CustomApiResponse logout(HttpServletRequest apiRequest) throws CustomException {
	  OAuth2Authentication authentication = ((OAuth2Authentication)apiRequest. getUserPrincipal());
      if (apiRequest.getHeader(Constants.ACCESS_TOKEN_HEADER.getValue()) != null) {
          String tokenValue = StringUtils.trim(StringUtils.replace(apiRequest.getHeader(Constants.ACCESS_TOKEN_HEADER.getValue()), Constants.ACCESS_TOKEN_STARTS_WITH.getValue(), ""));
          if(this.tokenService.revokeToken(tokenValue)) {
        	  LOGGER.info("Access Token and refresh token removed successfully for user: " + authentication.getUserAuthentication().getName());	
        	  return new CustomApiResponse().setMessage(StringUtils.trim(Messages.PROPERTIES.getPropertyValue("user.access.token.remove.success")) + ": " + authentication.getUserAuthentication().getName())
										.setStatusCode(HttpStatus.NO_CONTENT);  
          }
          else {
        	  LOGGER.error("Access Token and refresh token removal failed for user: " + authentication.getUserAuthentication().getName());
        	  throw new CustomException(StringUtils.trim(Messages.PROPERTIES.getPropertyValue("user.access.token.remove.error")) + ": " + authentication.getUserAuthentication().getName(), HttpStatus.NOT_FOUND);
          }
      } else {
    	  LOGGER.error("Exception while removing token for user: " + authentication.getUserAuthentication().getName());
    	  throw new CustomException(StringUtils.trim(Messages.PROPERTIES.getPropertyValue("user.access.token.remove.error")) + ": " + authentication.getUserAuthentication().getName(), HttpStatus.NOT_FOUND);
      }
  }
}
