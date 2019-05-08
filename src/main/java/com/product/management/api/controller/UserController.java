package com.product.management.api.controller;

import javax.servlet.http.HttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import com.product.management.api.common.Resources;
import com.product.management.api.domain.User;
import com.product.management.api.exception.CustomException;
import com.product.management.api.service.UserService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import com.product.management.api.model.CustomApiResponse;

@RestController
@RequestMapping(Resources.USER)
@Api(tags = "users")
@SuppressWarnings({"rawtypes", "unchecked"})
public class UserController {

private static final Logger LOGGER = LoggerFactory.getLogger(UserController.class);
	
  @Autowired private UserService userService;
  
  @Autowired private CustomApiResponse apiResponse;
  
  @RequestMapping(method = RequestMethod.POST, value = Resources.SIGNUP, 
		  					produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
  
  @ApiOperation(value = "${UserController.signup}")
  @ApiResponses(value = {
	@ApiResponse(code = 400, message = "Bad Request"),
	@ApiResponse(code = 403, message = "Access denied"),
	@ApiResponse(code = 422, message = "Invalid request payload"),
	@ApiResponse(code = 500, message = "Expired or invalid JWT token")})
  public ResponseEntity<CustomApiResponse> signup(@ApiParam("Signup User") @RequestBody User userData) throws CustomException{
	  		LOGGER.info("Inside UserController to create a new user with username: " + userData.getUsername());
	  		this.apiResponse = this.userService.signup(userData);
	  		return new ResponseEntity(this.apiResponse, this.apiResponse.getStatusCode());		
  }

  @RequestMapping(method = RequestMethod.DELETE, value = Resources.USERNAME, 
							produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
  @PreAuthorize("hasRole('ROLE_ADMIN')")
  @ApiOperation(value = "${UserController.delete}")
  @ApiResponses(value = {
	@ApiResponse(code = 400, message = "Something went wrong"),
	@ApiResponse(code = 403, message = "Access denied"),
	@ApiResponse(code = 404, message = "The user doesn't exist"),
	@ApiResponse(code = 500, message = "Expired or invalid JWT token")})
  public ResponseEntity<CustomApiResponse> deleteUser(@ApiParam("Username") @PathVariable String username) throws CustomException{
	  LOGGER.info("Inside UserController to delete a user with username: " + username);
	  this.apiResponse = this.userService.deleteUser(username);
	  return new ResponseEntity<CustomApiResponse>(this.apiResponse, this.apiResponse.getStatusCode());
  }

  @RequestMapping(method = RequestMethod.GET, value = Resources.USERNAME, 
							produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
  @PreAuthorize("hasRole('ROLE_ADMIN')")
  @ApiOperation(value = "${UserController.search}", response = User.class)
  @ApiResponses(value = {
      @ApiResponse(code = 400, message = "Something went wrong"),
      @ApiResponse(code = 403, message = "Access denied"),
      @ApiResponse(code = 404, message = "The user doesn't exist"),
      @ApiResponse(code = 500, message = "Expired or invalid JWT token")})
  public ResponseEntity search(@ApiParam("Username") @PathVariable String username) throws CustomException{
	  return new ResponseEntity<>(this.userService.searchUser(username), HttpStatus.OK);
  }
  
  @RequestMapping(method = RequestMethod.DELETE, value = Resources.LOGOUT, 
			produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
@PreAuthorize("hasRole('ROLE_ADMIN')")
@ApiOperation(value = "${UserController.search}", response = User.class)
@ApiResponses(value = {
@ApiResponse(code = 400, message = "Something went wrong"),
@ApiResponse(code = 403, message = "Access denied"),
@ApiResponse(code = 404, message = "The user doesn't exist"),
@ApiResponse(code = 500, message = "Expired or invalid JWT token")})
  public ResponseEntity<CustomApiResponse> logout(@ApiParam("Username") HttpServletRequest apiRequest) throws CustomException{
	  apiResponse  = this.userService.logout(apiRequest);
	  return new ResponseEntity<CustomApiResponse>(apiResponse, apiResponse.getStatusCode());
  }
}
