package com.product.management.api.service.impl;

import com.mongodb.WriteResult;
import com.product.management.api.common.Constants;
import com.product.management.api.model.CustomClientDetails;
import com.product.management.api.service.AppClientDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.provider.ClientAlreadyExistsException;
import org.springframework.security.oauth2.provider.ClientDetails;
import org.springframework.security.oauth2.provider.ClientRegistrationException;
import org.springframework.security.oauth2.provider.NoSuchClientException;
import java.util.ArrayList;
import java.util.List;

public class CustomClientDetailsService implements AppClientDetailsService {

    @Autowired
    private MongoTemplate mongoTemplate;
    
    @Autowired 
    private PasswordEncoder passwordEncoder;

    @Override
    public ClientDetails loadClientByClientId(String clientId) throws ClientRegistrationException {
        Query query = new Query();
        query.addCriteria(Criteria.where(Constants.CLIENT_ID.getValue()).is(clientId));
        CustomClientDetails clientDetails = mongoTemplate.findOne(query, CustomClientDetails.class);
        if (clientDetails == null) {
            throw new ClientRegistrationException(String.format("Client with id %s not found", clientId));
        }
        return clientDetails;
    }

    @Override
    public void addClientDetails(ClientDetails clientDetails) throws ClientAlreadyExistsException {
        if (this.loadClientByClientId(clientDetails.getClientId()) == null){
            CustomClientDetails mongoClientDetails = new CustomClientDetails()
            										.setClientId(clientDetails.getClientId())
            										.setResourceIds(clientDetails.getResourceIds())
            										.setSecretRequired(clientDetails.isSecretRequired())
            										.setClientSecret(passwordEncoder.encode(clientDetails.getClientSecret()))
            										.setScoped(clientDetails.isScoped())
            										.setScope(clientDetails.getScope())
            										.setAuthorizedGrantTypes(clientDetails.getAuthorizedGrantTypes())
            										.setRegisteredRedirectUri(clientDetails.getRegisteredRedirectUri())
            										.setAuthorities(clientDetails.getAuthorities())
            										.setAccessTokenValiditySeconds(clientDetails.getAccessTokenValiditySeconds())
            										.setRefreshTokenValiditySeconds(clientDetails.getRefreshTokenValiditySeconds())
            										.setAutoApprove(clientDetails.isAutoApprove("true"))
            										.setAdditionalInformation(clientDetails.getAdditionalInformation());
                            
            mongoTemplate.save(mongoClientDetails);
        } else {
            throw new ClientAlreadyExistsException(String.format("Client with id %s already existed",clientDetails.getClientId()));
        }
    }

    @Override
    public void updateClientDetails(ClientDetails clientDetails) throws NoSuchClientException {
        Query query = new Query();
        query.addCriteria(Criteria.where(Constants.CLIENT_ID.getValue()).is(clientDetails.getClientId()));
        Update update = new Update()
        				.set(Constants.RESOURCE_IDS.getValue(), clientDetails.getResourceIds())
        				.set(Constants.SCOPE.getValue(), clientDetails.getScope())
        				.set(Constants.AUTHORIZED_GRANT_TYPES.getValue(), clientDetails.getAuthorizedGrantTypes())
        				.set(Constants.REGISTERED_REDIRECT_URI.getValue(), clientDetails.getRegisteredRedirectUri())
        				.set(Constants.AUTHORITIES.getValue(), clientDetails.getAuthorities())
        				.set(Constants.ACCESS_TOKEN_VALIDITY_SECONDS.getValue(), clientDetails.getAccessTokenValiditySeconds())
        				.set(Constants.REFRESH_TOKEN_VALIDITY_SECONDS.getValue(), clientDetails.getRefreshTokenValiditySeconds())
        				.set(Constants.ADDITIONAL_INFORMATION.getValue(), clientDetails.getAdditionalInformation());

        WriteResult writeResult = mongoTemplate.updateFirst(query, update, CustomClientDetails.class);

        if(writeResult.getN() <= 0) {
            throw new NoSuchClientException(String.format("Client with id %s not found", clientDetails.getClientId()));
        }
    }

    @Override
    public void updateClientSecret(String clientId, String clientSecret) throws NoSuchClientException {
        Query query = new Query();
        query.addCriteria(Criteria.where(Constants.CLIENT_ID.getValue()).is(clientId));

        Update update = new Update();
        update.set(Constants.CLIENT_SECRET.getValue(), NoOpPasswordEncoder.getInstance().encode(clientSecret));

        WriteResult writeResult = mongoTemplate.updateFirst(query, update, CustomClientDetails.class);

        if(writeResult.getN() <= 0) {
            throw new NoSuchClientException(String.format("Client with id %s not found", clientId));
        }
    }

    @Override
    public void removeClientDetails(String clientId) throws NoSuchClientException {
        Query query = new Query();
        query.addCriteria(Criteria.where(Constants.CLIENT_ID.getValue()).is(clientId));

        WriteResult writeResult = mongoTemplate.remove(query, CustomClientDetails.class);

        if(writeResult.getN() <= 0) {
            throw new NoSuchClientException(String.format("Client with id %s not found", clientId));
        }
    }

    @Override
    public List<ClientDetails> listClientDetails() {
        List<ClientDetails> result =  new ArrayList<ClientDetails>();
        List<CustomClientDetails> details = mongoTemplate.findAll(CustomClientDetails.class);
        for (CustomClientDetails detail : details) {
            result.add(detail);
        }
        return result;
    }
}