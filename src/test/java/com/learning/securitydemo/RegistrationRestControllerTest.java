package com.learning.securitydemo;


import java.util.Map;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;

import com.learning.securitydemo.persistence.repository.AppUserRepository;
import com.learning.securitydemo.service.dto.RegistrationRequestDto;
import com.learning.securitydemo.util.PostgreSQLTestContainerExtension;


@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
@ExtendWith(PostgreSQLTestContainerExtension.class)
class RegistrationRestControllerTest {

    private static final String USERNAME_KEY = "username";
    private static final String VALID_USERNAME = "new_username@mail.com";
    private static final RegistrationRequestDto VALID_REGISTRATION_CREDENTIALS = new RegistrationRequestDto(
            VALID_USERNAME, "password", "password");
    @Autowired
    private AppUserRepository userRepository;
    @Autowired
    private TestRestTemplate restTemplate;

    @ParameterizedTest
    @ValueSource(strings = {"administrator", "journalist", "user"})
    void should_return201AndCorrectUsername_when_registerAdministratorWithValidCredentials(String registrationUrlType) {
        RegistrationRequestDto request = VALID_REGISTRATION_CREDENTIALS;
        HttpEntity<RegistrationRequestDto> requestEntity = new HttpEntity<>(request);
        ResponseEntity<Map<String, String>> responseEntity = restTemplate.exchange(
                "/register/" + registrationUrlType,
                HttpMethod.POST,
                requestEntity,
                new ParameterizedTypeReference<>() {
                });

        Map<String, String> response = responseEntity.getBody();
        Assertions.assertEquals(HttpStatus.CREATED, responseEntity.getStatusCode());
        Assertions.assertEquals(MediaType.APPLICATION_JSON, responseEntity.getHeaders().getContentType());
        Assertions.assertTrue(response.containsKey(USERNAME_KEY));
        Assertions.assertEquals(VALID_USERNAME, response.get(USERNAME_KEY));

        userRepository.findByUsername(VALID_USERNAME).ifPresent(userRepository::delete);
    }
    
}
