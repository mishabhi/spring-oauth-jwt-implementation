package com.product.management.api.service.impl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.product.management.api.domain.Role;
import com.product.management.api.domain.User;
import com.product.management.api.repository.UserRepository;

@Service
public class CustomUserDetailsService implements UserDetailsService {

  @Autowired
  private UserRepository userRepository;
	  
  @Override
  public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
    final User user = userRepository.findByUsername(username);

    if (user == null) {
      throw new UsernameNotFoundException("User '" + username + "' not found");
    }
    
    return org.springframework.security.core.userdetails.User
							    		.withUsername(username)
							    		.password(user.getPassword())								     
							    		.authorities(this.mapUserAuthorities(user.getRoles()))
								        .accountExpired(false)
								        .accountLocked(false)
								        .credentialsExpired(false)
								        .disabled(false)
								        .build();
  }
  
  private List<GrantedAuthority> mapUserAuthorities(List<String> userRoles){
	  List<GrantedAuthority> authorities = new ArrayList<>();
	  if(userRoles!=null && userRoles.size()!=0)
		  userRoles.forEach(role-> { authorities.add(Role.valueOf(role));});
	  return authorities;
  }
}
