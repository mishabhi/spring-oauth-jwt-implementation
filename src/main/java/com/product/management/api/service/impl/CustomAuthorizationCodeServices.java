package com.product.management.api.service.impl;

import com.product.management.api.common.Constants;
import com.product.management.api.model.AuthorizationCode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.security.oauth2.provider.code.RandomValueAuthorizationCodeServices;

public class CustomAuthorizationCodeServices extends RandomValueAuthorizationCodeServices {

    @Autowired
    private MongoTemplate mongoTemplate;

    @Override
    protected void store(String code, OAuth2Authentication authentication) {
    	AuthorizationCode authorizationCode = new AuthorizationCode();
        authorizationCode.setCode(code);
        authorizationCode.setAuthentication(authentication);
        mongoTemplate.save(authorizationCode);
    }

    @Override
    protected OAuth2Authentication remove(String code) {
        Query query = new Query();
        query.addCriteria(Criteria.where(Constants.AUTHORIZATION_CODE.getValue()).is(code));
        OAuth2Authentication authentication = null;
        AuthorizationCode authorizationCode = mongoTemplate.findOne(query, AuthorizationCode.class);
        if (authorizationCode != null) {
            authentication = authorizationCode.getAuthentication();
        }
        return authentication;
    }
}