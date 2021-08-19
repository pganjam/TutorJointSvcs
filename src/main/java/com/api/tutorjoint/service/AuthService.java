package com.api.tutorjoint.service;


import com.api.tutorjoint.security.services.UserDetailsImpl;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class AuthService {

    public Optional<UserDetailsImpl> getCurrentUser() {

        UserDetailsImpl principal = (UserDetailsImpl) SecurityContextHolder.
                getContext().getAuthentication().getPrincipal();

        return Optional.of(principal);
    }
}
