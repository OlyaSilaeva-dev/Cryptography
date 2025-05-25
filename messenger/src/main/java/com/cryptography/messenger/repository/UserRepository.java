package com.cryptography.messenger.repository;

import com.cryptography.messenger.model.User;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends MongoRepository<User, String> {
    Optional<User> findByUserName(String username);

    List<User> findByUserNameContainingIgnoreCase(String userName);
}
