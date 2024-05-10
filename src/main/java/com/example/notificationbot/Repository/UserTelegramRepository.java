package com.example.notificationbot.Repository;

import com.example.notificationbot.Entity.User;
import com.example.notificationbot.Entity.UserTelegram;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserTelegramRepository extends CrudRepository<UserTelegram, Long> {

    @Query(value = "select * from public.users_telegram ut left join public.user u on " +
            "ut.user_id = u.id where u.group = :group",nativeQuery = true)
    List<UserTelegram> findAllByGroup(String group);

    @Query(value = "select ut.chat_id from public.users_telegram ut left join public.user u on" +
            " ut.user_id = u.id where u.login=:username",nativeQuery = true)
    Optional<Long> findChatIdByUsername(String username);
    void deleteByChatId(Long chatId);

    Optional<UserTelegram> findUserTelegramByUser(User user);
}
