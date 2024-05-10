package com.example.notificationbot.Service;

import com.example.notificationbot.Entity.User;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public interface UserService {

    Optional<User> getUserByLogin(String login);
    void add(User user);
    User getUserById(Long id);
}
