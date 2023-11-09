package com.learning.securitydemo.util;

import org.junit.jupiter.api.extension.AfterAllCallback;
import org.junit.jupiter.api.extension.BeforeAllCallback;
import org.junit.jupiter.api.extension.ExtensionContext;

public class PostgreSQLTestContainerExtension implements BeforeAllCallback, AfterAllCallback {

    @Override
    public void beforeAll(ExtensionContext context) throws Exception {
        System.setProperty("spring.datasource.url", "jdbc:tc:postgresql:15-alpine:///");
        System.setProperty("spring.security.jwt.secret", "cofZUlayEW6vaGKwVDt5uNzr4vNWUg1F1BiPQe_EShfo4fT6oea1JzkYTpqT27pC");
        System.setProperty("sun.net.http.allowRestrictedHeaders", "true");
        System.setProperty("spring.security.cors.allowedOrigin", "none");
        System.setProperty("spring.security.cors.allowedMethod", "none");
        System.setProperty("spring.security.cors.allowedHeader", "none");
    }

    @Override
    public void afterAll(ExtensionContext context) throws Exception {
    }

}
