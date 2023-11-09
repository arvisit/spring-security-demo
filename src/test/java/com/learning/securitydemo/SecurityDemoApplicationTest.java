package com.learning.securitydemo;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.SqlGroup;

import com.learning.securitydemo.service.dto.AuthenticationRequestDto;
import com.learning.securitydemo.util.PostgreSQLTestContainerExtension;

@ExtendWith(PostgreSQLTestContainerExtension.class)
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
@SqlGroup({
        @Sql(scripts = "classpath:sql/add-users.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD),
        @Sql(scripts = "classpath:sql/delete-users.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD) })
class SecurityDemoApplicationTest {
    private static final String HELLO_ADMINISTRATOR_URL = "/hello/administrator";
    private static final String HELLO_JOURNALIST_URL = "/hello/journalist";
    private static final String HELLO_USER_URL = "/hello/user";
    private static final String LOGIN_URL = "/login";
    private static final AuthenticationRequestDto USER_WITH_ADMIN_AUTHORITY = new AuthenticationRequestDto(
            "john_doe@mail.com", "password");
    private static final AuthenticationRequestDto USER_WITH_JOURNALIST_AUTHORITY = new AuthenticationRequestDto(
            "jane_doe@mail.com", "password");
    private static final AuthenticationRequestDto USER_WITH_USER_AUTHORITY = new AuthenticationRequestDto(
            "janie_doe@mail.com", "password");

    @Autowired
    private TestRestTemplate restTemplate;

    @Test
    void shouldReturn200_whenRequestToAdministratorAuthorityEndpointWithValidAuthorities() {
        HttpEntity<String> requestEntity = new HttpEntity<>(getAuthHeader(USER_WITH_ADMIN_AUTHORITY));
        ResponseEntity<String> response = restTemplate.exchange(
                HELLO_ADMINISTRATOR_URL,
                HttpMethod.GET,
                requestEntity,
                String.class);

        Assertions.assertEquals(HttpStatus.OK, response.getStatusCode());
    }
    
    @Test
    void shouldReturn404_whenRequestToNonExistingEndpointWithValidAuthorities() {
        HttpEntity<String> requestEntity = new HttpEntity<>(getAuthHeader(USER_WITH_ADMIN_AUTHORITY));
        ResponseEntity<String> response = restTemplate.exchange(
                "/hello/blah",
                HttpMethod.GET,
                requestEntity,
                String.class);
        
        Assertions.assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    @Test
    void shouldReturn200_whenRequestToJournalistAuthorityEndpointWithValidAuthorities() {
        HttpEntity<String> requestEntity = new HttpEntity<>(getAuthHeader(USER_WITH_JOURNALIST_AUTHORITY));
        ResponseEntity<String> response = restTemplate.exchange(
                HELLO_JOURNALIST_URL,
                HttpMethod.GET,
                requestEntity,
                String.class);

        Assertions.assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Test
    void shouldReturn200_whenRequestToUserAuthorityEndpointWithValidAuthorities() {
        HttpEntity<String> requestEntity = new HttpEntity<>(getAuthHeader(USER_WITH_USER_AUTHORITY));
        ResponseEntity<String> response = restTemplate.exchange(
                HELLO_USER_URL,
                HttpMethod.GET,
                requestEntity,
                String.class);

        Assertions.assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Test
    void shouldReturn403_whenRequestToJournalistAuthorityEndpointWithInvalidAuthorities() {
        HttpEntity<String> requestEntity = new HttpEntity<>(getAuthHeader(USER_WITH_USER_AUTHORITY));
        ResponseEntity<String> response = restTemplate.exchange(
                HELLO_JOURNALIST_URL,
                HttpMethod.GET,
                requestEntity,
                String.class);

        Assertions.assertEquals(HttpStatus.FORBIDDEN, response.getStatusCode());
    }

    private HttpHeaders getAuthHeader(AuthenticationRequestDto request) {
        HttpEntity<AuthenticationRequestDto> requestEntity = new HttpEntity<>(request);
        ResponseEntity<String> response = restTemplate.exchange(
                LOGIN_URL,
                HttpMethod.POST,
                requestEntity,
                String.class);

        HttpHeaders headers = new HttpHeaders();
        String token = "Bearer " + response.getHeaders().getFirst(HttpHeaders.AUTHORIZATION);
        headers.add(HttpHeaders.AUTHORIZATION, token);
        return headers;
    }
}
