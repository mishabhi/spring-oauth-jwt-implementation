package com.product.management.api.common;


import java.util.Properties;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public enum Messages {
    PROPERTIES;

    private static final String FILE = "/message.properties";
    private static final Logger LOGGER = LoggerFactory.getLogger(Messages.class);
    private static Properties properties = new Properties();

    private Properties init() {
        if (properties.size()==0) {
            try {
                properties.load(Messages.class.getResourceAsStream(FILE));
            }catch (Exception expObje) {
            	LOGGER.error("Unable to load " + FILE + " file from classpath.", expObje);
                System.exit(1);
            }
        }
        return properties;
    }

    public String getPropertyValue(String propertyKey) {
        return this.init().get(propertyKey).toString();
    }
}
