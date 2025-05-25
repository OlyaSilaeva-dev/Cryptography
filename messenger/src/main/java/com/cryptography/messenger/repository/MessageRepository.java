package com.cryptography.messenger.repository;

import com.cryptography.messenger.model.Message;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MessageRepository extends MongoRepository<Message, String> {
    List<Message> findAllByRoomId(String roomId);

    List<Message> findByRoomId(String roomId);
}
