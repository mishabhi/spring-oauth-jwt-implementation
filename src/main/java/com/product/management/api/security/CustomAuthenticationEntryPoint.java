package com.product.management.api.security;

import java.io.IOException;
import java.util.Date;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;

public class CustomAuthenticationEntryPoint implements AuthenticationEntryPoint {

    @Override
    public void commence(HttpServletRequest httpRequest, HttpServletResponse httpResponse, AuthenticationException authException) throws IOException, ServletException {
        httpResponse.setContentType("application/json;charset=UTF-8");
        httpResponse.setStatus(403);
        try {
			httpResponse.getWriter().write(new JSONObject()
									        .put("timestamp", new Date().getTime())
									        .put("status", HttpStatus.UNAUTHORIZED)
									        .put("message", "Access Denied")
									        .toString());
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    }
}
