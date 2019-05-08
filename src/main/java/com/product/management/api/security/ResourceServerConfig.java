package com.product.management.api.security;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;
import org.springframework.security.oauth2.config.annotation.web.configuration.ResourceServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configurers.ResourceServerSecurityConfigurer;
import org.springframework.security.oauth2.provider.error.OAuth2AccessDeniedHandler;
import org.springframework.security.oauth2.provider.token.DefaultTokenServices;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.security.web.util.matcher.RequestMatcher;
import com.product.management.api.common.Resources;


@Configuration
@EnableResourceServer
@EnableGlobalMethodSecurity(prePostEnabled=true)
public class ResourceServerConfig extends ResourceServerConfigurerAdapter {
	
	@Autowired
	@Qualifier(value = "tokenService")
	private DefaultTokenServices tokenService;
	
	@Autowired private TokenStore tokenStore;
	
	@Override
	public void configure(HttpSecurity http) throws Exception {
		http.requestMatcher(new OAuthRequestedMatcher())
        	.anonymous().disable()
        	.authorizeRequests()
        	.antMatchers(HttpMethod.OPTIONS).permitAll()
        	.antMatchers(HttpMethod.GET, Resources.USER + "/**").permitAll()
        	/*.antMatchers(HttpMethod.GET, "/user/{userid}").access("hasAnyRole('ROLE_ADMIN')")
        	.antMatchers(HttpMethod.DELETE, "/user/{userid}").hasAnyRole("ROLE_ADMIN")
        	.antMatchers("/api/register").hasAuthority("ROLE_ADMIN")*/
        	.and().exceptionHandling().accessDeniedHandler(new OAuth2AccessDeniedHandler());
	}
	
	@Override
    public void configure(ResourceServerSecurityConfigurer resources) {
		resources.resourceId("app-resource-1")
				 .resourceId("app-resource-2")
				 .tokenStore(this.tokenStore)
				 .tokenServices(this.tokenService);
    }
	
	private static class OAuthRequestedMatcher implements RequestMatcher {
        @Override
		public boolean matches(HttpServletRequest request) {
            String auth = request.getHeader("Authorization");
            boolean hasOauth2Token = (auth != null) && auth.startsWith("Bearer");
            boolean hasAccessToken = request.getParameter("access_token")!=null;
            return hasOauth2Token || hasAccessToken;
        }
    }

}