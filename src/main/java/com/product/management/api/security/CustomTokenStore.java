package com.product.management.api.security;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.security.oauth2.common.OAuth2RefreshToken;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.security.oauth2.provider.token.AuthenticationKeyGenerator;
import org.springframework.security.oauth2.provider.token.store.JwtAccessTokenConverter;
import org.springframework.security.oauth2.provider.token.store.JwtTokenStore;

import com.product.management.api.common.Constants;
import com.product.management.api.model.AccessToken;
import com.product.management.api.model.MongoRefreshToken;
import java.io.UnsupportedEncodingException;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class CustomTokenStore extends JwtTokenStore {
	
	@Autowired
	private AuthenticationKeyGenerator authenticationKeyGenerator;

    @Autowired
    private MongoTemplate mongoTemplate;
    
    public CustomTokenStore(JwtAccessTokenConverter jwtTokenEnhancer) {
		super(jwtTokenEnhancer);
	}

    @Override
    public OAuth2Authentication readAuthentication(OAuth2AccessToken accessToken) {
        return readAuthentication(accessToken.getValue());
    }

    @Override
    public OAuth2Authentication readAuthentication(String token) {
        Query query = new Query();
        query.addCriteria(Criteria.where(Constants.TOKEN_ID.getValue()).is(extractTokenKey(token)));

        AccessToken AccessToken = mongoTemplate.findOne(query, AccessToken.class);
        return AccessToken != null ? AccessToken.getAuthentication() : null;
    }

    @Override
    public void storeAccessToken(OAuth2AccessToken accessToken, OAuth2Authentication authentication) {
        String refreshToken = null;
        if (accessToken.getRefreshToken() != null) {
            refreshToken = accessToken.getRefreshToken().getValue();
        }

        if (readAccessToken(accessToken.getValue()) != null) {
            this.removeAccessToken(accessToken);
        }

        AccessToken AccessToken = new AccessToken();
        AccessToken.setTokenId(extractTokenKey(accessToken.getValue()));
        AccessToken.setToken(accessToken);
        AccessToken.setAuthenticationId(authenticationKeyGenerator.extractKey(authentication));
        AccessToken.setUsername(authentication.isClientOnly() ? null : authentication.getName());
        AccessToken.setClientId(authentication.getOAuth2Request().getClientId());
        AccessToken.setAuthentication(authentication);
        AccessToken.setRefreshToken(extractTokenKey(refreshToken));

        mongoTemplate.save(AccessToken);
    }

    @Override
    public OAuth2AccessToken readAccessToken(String tokenValue) {
        Query query = new Query();
        query.addCriteria(Criteria.where(Constants.TOKEN_ID.getValue()).is(extractTokenKey(tokenValue)));

        AccessToken AccessToken = mongoTemplate.findOne(query, AccessToken.class);
        return AccessToken != null ? AccessToken.getToken() : null;
    }

    @Override
    public void removeAccessToken(OAuth2AccessToken oAuth2AccessToken) {
        Query query = new Query();
        query.addCriteria(Criteria.where(Constants.TOKEN_ID.getValue()).is(extractTokenKey(oAuth2AccessToken.getValue())));
        mongoTemplate.remove(query, AccessToken.class);
    }

    @Override
    public void storeRefreshToken(OAuth2RefreshToken refreshToken, OAuth2Authentication authentication) {
        MongoRefreshToken token = new MongoRefreshToken();
        token.setTokenId(extractTokenKey(refreshToken.getValue()));
        token.setToken(refreshToken);
        token.setAuthentication(authentication);
        mongoTemplate.save(token);
    }

    @Override
    public OAuth2RefreshToken readRefreshToken(String tokenValue) {
        Query query = new Query();
        query.addCriteria(Criteria.where(Constants.TOKEN_ID.getValue()).is(extractTokenKey(tokenValue)));

        MongoRefreshToken mongoRefreshToken = mongoTemplate.findOne(query, MongoRefreshToken.class);
        return mongoRefreshToken != null ? mongoRefreshToken.getToken() : null;
    }

    @Override
    public OAuth2Authentication readAuthenticationForRefreshToken(OAuth2RefreshToken refreshToken) {
        Query query = new Query();
        query.addCriteria(Criteria.where(Constants.TOKEN_ID.getValue()).is(extractTokenKey(refreshToken.getValue())));

        MongoRefreshToken mongoRefreshToken = mongoTemplate.findOne(query, MongoRefreshToken.class);
        return mongoRefreshToken != null ? mongoRefreshToken.getAuthentication() : null;
    }

    @Override
    public void removeRefreshToken(OAuth2RefreshToken refreshToken) {
        Query query = new Query();
        query.addCriteria(Criteria.where(Constants.TOKEN_ID.getValue()).is(extractTokenKey(refreshToken.getValue())));
        mongoTemplate.remove(query, MongoRefreshToken.class);
    }

    @Override
    public void removeAccessTokenUsingRefreshToken(OAuth2RefreshToken refreshToken) {
        Query query = new Query();
        query.addCriteria(Criteria.where(Constants.REFRESH_TOKEN.getValue()).is(extractTokenKey(refreshToken.getValue())));
        mongoTemplate.remove(query, AccessToken.class);
    }

    @Override
    public OAuth2AccessToken getAccessToken(OAuth2Authentication authentication) {
        OAuth2AccessToken accessToken = null;
        String authenticationId = authenticationKeyGenerator.extractKey(authentication);

        Query query = new Query();
        query.addCriteria(Criteria.where(Constants.AUTHENTICATION_ID.getValue()).is(authenticationId));

        AccessToken AccessToken = mongoTemplate.findOne(query, AccessToken.class);
        if (AccessToken != null) {
            accessToken = AccessToken.getToken();
            if(accessToken != null && !authenticationId.equals(this.authenticationKeyGenerator.extractKey(this.readAuthentication(accessToken)))) {
                this.removeAccessToken(accessToken);
                this.storeAccessToken(accessToken, authentication);
            }
        }
        return accessToken;
    }

    @Override
    public Collection<OAuth2AccessToken> findTokensByClientIdAndUserName(String clientId, String username) {
        Collection<OAuth2AccessToken> tokens = new ArrayList<OAuth2AccessToken>();
        Query query = new Query();
        query.addCriteria(Criteria.where(Constants.CLIENT_ID.getValue()).is(clientId));
        query.addCriteria(Criteria.where(Constants.USERNAME.getValue()).is(username));
        List<AccessToken> accessTokens = mongoTemplate.find(query, AccessToken.class);
        for (AccessToken accessToken : accessTokens) {
            tokens.add(accessToken.getToken());
        }
        return tokens;
    }

    @Override
    public Collection<OAuth2AccessToken> findTokensByClientId(String clientId) {
        Collection<OAuth2AccessToken> tokens = new ArrayList<OAuth2AccessToken>();
        Query query = new Query();
        query.addCriteria(Criteria.where(Constants.CLIENT_ID.getValue()).is(clientId));
        List<AccessToken> accessTokens = mongoTemplate.find(query, AccessToken.class);
        for (AccessToken accessToken : accessTokens) {
            tokens.add(accessToken.getToken());
        }
        return tokens;
    }

    private String extractTokenKey(String value) {
        if(value == null) {
            return null;
        } else {
            MessageDigest digest;
            try {
                digest = MessageDigest.getInstance("MD5");
            } catch (NoSuchAlgorithmException var5) {
                throw new IllegalStateException("MD5 algorithm not available.  Fatal (should be in the JDK).");
            }

            try {
                byte[] e = digest.digest(value.getBytes("UTF-8"));
                return String.format("%032x", new Object[]{new BigInteger(1, e)});
            } catch (UnsupportedEncodingException var4) {
                throw new IllegalStateException("UTF-8 encoding not available.  Fatal (should be in the JDK).");
            }
        }
    }
}