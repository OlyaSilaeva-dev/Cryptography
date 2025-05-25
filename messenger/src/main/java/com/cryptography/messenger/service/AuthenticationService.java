package com.cryptography.messenger.service;

import com.cryptography.messenger.dto.auth.AuthRequest;
import com.cryptography.messenger.dto.auth.AuthResponse;
import com.cryptography.messenger.exceptions.UnknownUserException;
import com.cryptography.messenger.exceptions.UserAlreadyExistsException;
import com.cryptography.messenger.model.Role;
import com.cryptography.messenger.model.User;
import com.cryptography.messenger.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.*;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
@RequiredArgsConstructor
public class AuthenticationService {

    private final UserRepository repository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;

    public AuthResponse register(AuthRequest request) {
        User user = User.builder()
                .userName(request.getUserName())
                .password(passwordEncoder.encode(request.getPassword()))
                .role(Role.USER)
        .build();
        if(repository.findByUserName(request.getUserName()).isPresent()) {
            throw new UserAlreadyExistsException();
        }
        repository.save(user);
        var jwtToken = jwtService.generateToken(Map.of("role",user.getRole()) , user);
        return new AuthResponse(jwtToken);
    }


    public AuthResponse authentication(AuthRequest request) {
        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            request.getUserName(),
                            request.getPassword()
                    )
            );
        } catch (BadCredentialsException e) {
            throw new BadCredentialsException("Invalid email or password");
        }
        User user = repository.findByUserName(request.getUserName()).orElseThrow(UnknownUserException::new);
        var jwtToken = jwtService.generateToken(Map.of("role", user.getRole()), user);
        return new AuthResponse(jwtToken);
    }
}
