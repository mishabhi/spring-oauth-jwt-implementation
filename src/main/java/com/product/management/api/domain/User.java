package com.product.management.api.domain;


import java.util.List;
import javax.validation.constraints.NotNull;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

@Document(collection = "users")
@JsonInclude(Include.NON_NULL) 
public class User {

	@Id
    @NotNull
    @Field("id")
    private String username;
    
    private String firstName;

    private String lastName;

    private String email;
    
    private String password;
    
    private List<String> roles;
   
    public String getUsername() {
        return this.username;
    }

    public User setUsername(String username) {
        this.username = username;
        return this;
    }

    public String getFirstName() {
        return this.firstName;
    }

    public User setFirstName(String firstName) {
        this.firstName = firstName;
        return this;
    }

    public String getLastName() {
        return this.lastName;
    }

    public User setLastName(String lastName) {
        this.lastName = lastName;
        return this;
    }

    public String getEmail() {
        return this.email;
    }

    public User setEmail(String email) {
        this.email = email;
        return this;
    }
    
    public String getPassword() {
        return this.password;
    }

    public User setPassword(String password) {
        this.password = password;
        return this;
    }
    
    public List<String> getRoles() {
        return this.roles;
    }

    public User setRoles(List<String> roles) {
        this.roles = roles;
        return this;
    }
    
    @Override
    public String toString() {
        return "User{" +
                "id=" + this.username +
                ", firstName='" + this.firstName + '\'' +
                ", lastName='" + this.lastName + '\'' +
                ", email='" + this.email + '\'' +
                '}';
    }
}
