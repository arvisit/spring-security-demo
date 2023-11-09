package com.learning.securitydemo.service;

import static org.springframework.security.core.userdetails.User.withUsername;

import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.learning.securitydemo.persistence.model.AppUser;
import com.learning.securitydemo.persistence.repository.AppUserRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AppUserDetailsService implements UserDetailsService {

    private final AppUserRepository appUserRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        AppUser user = appUserRepository.findByUsername(username).orElseThrow(
                () -> new UsernameNotFoundException(String.format("User with usrename '%s' not found!", username)));
        boolean isDisabled = !user.getEnabled();

        return withUsername(user.getUsername())
                .password(user.getPassword())
                .disabled(isDisabled)
                .accountExpired(isDisabled)
                .accountLocked(isDisabled)
                .credentialsExpired(isDisabled)
                .authorities(user.getRoles().stream()
                        .map(role -> new SimpleGrantedAuthority("ROLE_" + role.name()))
                        .toList())
                .build();
    }

}
