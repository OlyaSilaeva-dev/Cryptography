package com.cryptography.messenger.service;

import com.cryptography.messenger.model.User;
import com.cryptography.messenger.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository repository;
    private final UserRepository userRepository;

    public void save(User user) {
        userRepository.save(user);
    }

    public Optional<User> findByUsername(String username) {
        return repository.findByUserName(username);
    }

    public Optional<User> findById(String id) {
        return repository.findById(id);
    }

    public List<User> findAll() {
        return repository.findAll();
    }

    public List<User> searchByUsername(String query) {
        return repository.findByUserNameContainingIgnoreCase(query);
    }
}
