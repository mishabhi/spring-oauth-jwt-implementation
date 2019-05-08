package com.product.management.api.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.security.SecurityProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.provider.ClientDetailsService;
import org.springframework.security.oauth2.provider.approval.ApprovalStore;
import org.springframework.security.oauth2.provider.approval.TokenApprovalStore;
import org.springframework.security.oauth2.provider.approval.TokenStoreUserApprovalHandler;
import org.springframework.security.oauth2.provider.request.DefaultOAuth2RequestFactory;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.security.web.AuthenticationEntryPoint;
import com.product.management.api.common.Resources;

@Configuration
@EnableWebSecurity
@Order(SecurityProperties.ACCESS_OVERRIDE_ORDER)
public class AppSecurityConfiguration	 extends WebSecurityConfigurerAdapter {

  @Autowired private AuthenticationEntryPoint authEntryPoint;
  
  @Autowired private UserDetailsService userDetails;
  
  @Autowired private ClientDetailsService clientDetailsService;
   
  @Autowired private PasswordEncoder passwordEncoder;

  @Override
  protected void configure(HttpSecurity http) throws Exception {
		
	  http.csrf().disable()
	  	  .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);
	  http.authorizeRequests()
	      .antMatchers(HttpMethod.POST, Resources.LOGIN).permitAll()
	      .antMatchers(HttpMethod.POST, Resources.USER + Resources.SIGNUP).permitAll()
	      .anyRequest().fullyAuthenticated()
	      .and().exceptionHandling().authenticationEntryPoint(authEntryPoint);	      	        
  }

  @Override
  public void configure(WebSecurity web) throws Exception {
	  web.ignoring().antMatchers("/v2/api-docs")
	  	 .antMatchers("/swagger-resources/**")
	  	 .antMatchers("/swagger-ui.html")
	  	 .antMatchers("/configuration/**")
	  	 .antMatchers("/webjars/**")
	  	 .antMatchers("/public");
  }
  
  @Override
  @Bean
  public AuthenticationManager authenticationManagerBean() throws Exception {
      return super.authenticationManagerBean();
  }
  
  @Bean
  @Autowired
  public TokenStoreUserApprovalHandler userApprovalHandler(TokenStore tokenStore) {
	  TokenStoreUserApprovalHandler handler = new TokenStoreUserApprovalHandler();
	  handler.setTokenStore(tokenStore);
	  handler.setRequestFactory(new DefaultOAuth2RequestFactory(this.clientDetailsService));
	  handler.setClientDetailsService(this.clientDetailsService);
	  return handler;
  }

  @Bean
  @Autowired
  public ApprovalStore approvalStore(TokenStore tokenStore) throws Exception {
	  TokenApprovalStore store = new TokenApprovalStore();
	  store.setTokenStore(tokenStore);
	  return store;
  }
  
@Override
  protected void configure(AuthenticationManagerBuilder auth) throws Exception {
      auth.userDetailsService(this.userDetails)
          .and()
          .authenticationProvider(authenticationProvider());

  }
  
  @Bean
  public AuthenticationProvider authenticationProvider() {
	  DaoAuthenticationProvider authenticationProvider = new DaoAuthenticationProvider();
	  authenticationProvider.setUserDetailsService(this.userDetails);
	  authenticationProvider.setPasswordEncoder(this.passwordEncoder);
	  return authenticationProvider;
  }
}
