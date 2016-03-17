package com.zachpuls.website;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import java.io.IOException;
import java.util.Properties;

/**
 * Created by jenni on 3/16/2016.
 */

public class PropertyLoader implements ServletContextListener {

    @Override
    public void contextInitialized(ServletContextEvent servletContextEvent) {

        Properties properties = new Properties();

        try {
            properties.load(servletContextEvent.getServletContext().getResourceAsStream("/WEB-INF/captcha-key.properties"));
        } catch (IOException e) {
            e.printStackTrace();
        }

        for (String property : properties.stringPropertyNames()) {
            servletContextEvent.getServletContext().setInitParameter(property, properties.getProperty(property));
        }
    }

    @Override
    public void contextDestroyed(ServletContextEvent servletContextEvent) {
    }
}
