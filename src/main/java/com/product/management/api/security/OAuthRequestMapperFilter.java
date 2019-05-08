package com.product.management.api.security;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Objects;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import org.apache.catalina.connector.RequestFacade;
import org.springframework.core.annotation.Order;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.core.JsonParser.Feature;

@Component
@Order(value = Integer.MIN_VALUE)
public class OAuthRequestMapperFilter implements Filter {

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
    	
    }

    @SuppressWarnings("unchecked")
	@Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException,
            ServletException {
        if (Objects.equals(((HttpServletRequest)request).getContentType(), MediaType.APPLICATION_JSON_VALUE) && ((HttpServletRequest)request).getServletPath().contains("/oauth/token")) {
            InputStream is = request.getInputStream();
            ByteArrayOutputStream buffer = new ByteArrayOutputStream();

            int nRead;
            byte[] data = new byte[16384];

            while ((nRead = is.read(data, 0, data.length)) != -1) {
                buffer.write(data, 0, nRead);
            }
            buffer.flush();
            byte[] json = buffer.toByteArray();
            
            ObjectMapper objectMapper = new ObjectMapper();
            objectMapper.configure(Feature.AUTO_CLOSE_SOURCE, true);

            HashMap<String, String> result = objectMapper.readValue(json, HashMap.class);
            HashMap<String, String[]> r = new HashMap<>();
            for (String key : result.keySet()) {
                String[] val = new String[1];
                val[0] = result.get(key);
                r.put(key, val);
            }

            String[] val = new String[1];
            val[0] = ((RequestFacade) request).getMethod();
            r.put("_method", val);

            HttpServletRequest s = new CustomRequestWrapper(((HttpServletRequest) request), r);
            chain.doFilter(s, response);
        } else {
            chain.doFilter(request, response);
        }
    }

    @Override
    public void destroy() {
    }
}