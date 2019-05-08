package com.product.management.api.helper;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.converter.Converter;
import com.mongodb.DBObject;
import com.product.management.api.domain.User;


@SuppressWarnings("unchecked")
public class UserDataReaderConverter implements Converter<DBObject, User>{
	
	@Override
	@Autowired
	public User convert(DBObject userData) {
		User user = new User();
		if(Objects.isNull(userData.get("_id")) || Objects.isNull(userData.get("password"))) {
			return user;
		}
		return user.setUsername(userData.get("_id").toString())
					.setPassword(userData.get("password").toString())
					.setEmail(userData.get("email")!=null ? userData.get("email").toString() : null)
					.setRoles(((List<String>)userData.get("roles")).size()!=0 ? (List<String>)userData.get("roles") : new ArrayList<String>())
					.setFirstName(userData.get("firstName")!=null ? userData.get("firstName").toString() : null)
					.setLastName(userData.get("lastName")!=null ? userData.get("lastName").toString() : null);
	}
}
