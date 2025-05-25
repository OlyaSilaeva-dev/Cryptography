package com.cryptography.messenger.repository;

import com.cryptography.messenger.model.ChatRoom;
import com.cryptography.messenger.model.User;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface ChatRoomRepository extends MongoRepository<ChatRoom, String> {

      boolean existsByFirstUserIdAndSecondUserId(String firstUserId, String secondUserId);
}
