package com.example.notificationbot.Repository;

import com.example.notificationbot.Entity.User;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends CrudRepository<User,Long> {

    @Query(value = "select * from public.user where login = :login",nativeQuery = true)
    Optional<User> findUserByLogin(String login);
}
