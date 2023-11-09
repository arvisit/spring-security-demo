package com.learning.securitydemo.service;

import java.util.Set;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.learning.securitydemo.exception.UsernameAlreadyExistsException;
import com.learning.securitydemo.persistence.model.AppUser;
import com.learning.securitydemo.persistence.model.RoleEnum;
import com.learning.securitydemo.persistence.repository.AppUserRepository;
import com.learning.securitydemo.service.dto.RegistrationRequestDto;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class RegistrationService {

    private final AppUserRepository appUserRepository;
    private final PasswordEncoder passwordEncoder;

    @Transactional
    public String register(RegistrationRequestDto credentials) {
        String username = credentials.username();
        if (appUserRepository.findByUsername(username).isPresent()) {
            throw new UsernameAlreadyExistsException(String.format("Username '%s' already exists", username));
        }
        AppUser userToCreate = AppUser.builder().withUsername(username)
                .withPassword(passwordEncoder.encode(credentials.password()))
                .withRoles(Set.of(RoleEnum.USER))
                .withEnabled(true)
                .build();
        return appUserRepository.save(userToCreate)
                .getUsername();
    }

    @Transactional
    public String registerAdministrator(RegistrationRequestDto credentials) {
        return register(credentials, RoleEnum.ADMINISTRATOR);
    }

    @Transactional
    public String registerJournalist(RegistrationRequestDto credentials) {
        return register(credentials, RoleEnum.JOURNALIST);
    }

    @Transactional
    public String registerUser(RegistrationRequestDto credentials) {
        return register(credentials, RoleEnum.USER);
    }

    private String register(RegistrationRequestDto credentials, RoleEnum role) {
        String username = credentials.username();
        if (appUserRepository.findByUsername(username).isPresent()) {
            throw new UsernameAlreadyExistsException(String.format("Username '%s' already exists", username));
        }
        AppUser userToCreate = AppUser.builder().withUsername(username)
                .withPassword(passwordEncoder.encode(credentials.password()))
                .withRoles(Set.of(role))
                .withEnabled(true)
                .build();
        return appUserRepository.save(userToCreate)
                .getUsername();
    }
}
