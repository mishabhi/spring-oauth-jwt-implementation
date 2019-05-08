package com.product.management.api.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.oauth2.config.annotation.configurers.ClientDetailsServiceConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configuration.AuthorizationServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableAuthorizationServer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerEndpointsConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerSecurityConfigurer;
import org.springframework.security.oauth2.provider.ClientDetailsService;
import org.springframework.security.oauth2.provider.code.AuthorizationCodeServices;
import org.springframework.security.oauth2.provider.token.DefaultTokenServices;
import org.springframework.security.oauth2.provider.token.TokenEnhancerChain;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.security.oauth2.provider.token.store.JwtAccessTokenConverter;
import java.util.Arrays;

@Configuration
@EnableAuthorizationServer
public class AuthorizationServerConfig extends AuthorizationServerConfigurerAdapter {

    @Autowired private AuthenticationManager authenticationManager;
    
    @Autowired private TokenStore tokenStore;
    
    @Autowired(required = true) private JwtAccessTokenConverter tokenEnhancer;
    
    @Autowired(required = true)
    @Qualifier(value = "customClientDetailsService")
    private ClientDetailsService clientDetailsService;
    
    @Autowired private AuthorizationCodeServices authCodeService;
    
    @Autowired
    @Qualifier(value = "tokenService")
    private DefaultTokenServices tokenService;
    
    @Autowired private UserDetailsService userDetails;

    @Override
    public void configure(final AuthorizationServerSecurityConfigurer oauthServer) throws Exception {
        oauthServer.tokenKeyAccess("permitAll()")
            	   .checkTokenAccess("isAuthenticated()")
            	   .allowFormAuthenticationForClients();
    }
    
    @Override
    public void configure(final ClientDetailsServiceConfigurer clients) throws Exception {
    	clients.withClientDetails(this.clientDetailsService);
    	/*clients.inMemory()
				.withClient("web-client")
				.secret("web-client-secret")
				.authorizedGrantTypes("password", "authorization_code", "refresh_token", "implicit" )
				.scopes("read", "write", "trust")
				.accessTokenValiditySeconds(1*60*60).
				refreshTokenValiditySeconds(6*60*60);*/
    }
    
    @Override
    public void configure(AuthorizationServerEndpointsConfigurer endpoints) throws Exception {
    	final TokenEnhancerChain tokenEnhancerChain = new TokenEnhancerChain();
    	tokenEnhancerChain.setTokenEnhancers(Arrays.asList(this.tokenEnhancer));
    	endpoints.authorizationCodeServices(authCodeService)
                 .tokenServices(this.tokenService)
                 .userDetailsService(this.userDetails)
                 .accessTokenConverter(this.tokenEnhancer)
                 .reuseRefreshTokens(true)
                 .tokenStore(this.tokenStore) 
                 .authenticationManager(this.authenticationManager);
    }
}