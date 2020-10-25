package com.siwoo.util;

import com.siwoo.repository.ToDoRepository;

import java.io.IOException;
import java.net.URL;
import java.util.Properties;

public enum AppProperties {
    INSTANCE("application.properties");

    private final Properties props;

    AppProperties(String file) {
        try {
            URL url = AppProperties.class.getClassLoader().getResource(file);
            props = new Properties();
            props.load(url.openStream());
        } catch (IOException e) {
            throw new IllegalStateException("Not found properties file.");
        }
    }

    public String get(String key) {
        return props.getProperty(key);
    }
}
