package com.product.management.api.service;

import javax.servlet.http.HttpServletRequest;

import com.product.management.api.domain.User;
import com.product.management.api.exception.CustomException;
import com.product.management.api.model.CustomApiResponse;

public interface UserService {
	public CustomApiResponse signup(User user) throws CustomException;
	public CustomApiResponse deleteUser(String username) throws CustomException;
	public User searchUser(String username) throws CustomException;
	public CustomApiResponse logout(HttpServletRequest apiRequest) throws CustomException;
}
