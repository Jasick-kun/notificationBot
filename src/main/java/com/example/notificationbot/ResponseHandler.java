package com.example.notificationbot;


import com.example.notificationbot.Entity.User;
import com.example.notificationbot.Entity.UserTelegram;
import com.example.notificationbot.Repository.UserTelegramRepository;
import com.example.notificationbot.Service.Impl.UserServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.telegram.abilitybots.api.db.DBContext;
import org.telegram.abilitybots.api.sender.SilentSender;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;

import java.util.Map;
import java.util.Optional;

import static com.example.notificationbot.Constants.START_TEXT;


@RequiredArgsConstructor
public class ResponseHandler {

    private final UserTelegramRepository  userTelegramRepository;

    private final UserServiceImpl userService;
    private final SilentSender sender;
    private final Map<Long, UserState> chatStates;


    public void replyToStart(Long chatId) {
        SendMessage message = new SendMessage();
        message.setChatId(chatId);
        message.setText(START_TEXT);
        sender.execute(message);
        chatStates.put(chatId, UserState.START);
    }
    public void replyToButtons(Long chatId, Message message) {

        if (message.getText().equals("/start")) {
            chatStates.put(chatId, UserState.START);

        }
        switch (chatStates.get(chatId)){
            case START -> auth(chatId,message);
            case AWAITING_AUTH -> loginCheck(chatId,message);
            case WORK -> messageHandler(chatId,message);
            default -> unexpectedMessage(chatId);
        }
    }
    private void unexpectedMessage(Long chatId) {
        SendMessage sendMessage = new SendMessage();
        sendMessage.setChatId(chatId);
        sendMessage.setText("I did not expect that.");
        sender.execute(sendMessage);
    }
    private void auth(Long chatId,Message message){
        SendMessage sendMessage = new SendMessage();
        sendMessage.setChatId(chatId);
        sendMessage.setText("Для авторизации введите логин и пароль в формате: \n" +
                "логин\nпароль");
        sender.execute(sendMessage);
        chatStates.put(chatId, UserState.AWAITING_AUTH);
    }
    private void loginCheck(Long chatId,Message message){
        SendMessage sendMessage = new SendMessage();
        sendMessage.setChatId(chatId);
        String username = message.getText().split("\n")[0];
        String password = message.getText().split("\n")[1];
        Optional<User> user = userService.getUserByLogin(username);
        if(user.isEmpty()){
            sendMessage.setText("Пользователя с таким логином не существует");
        }
        else if(user.get().getPassword().equals(password)){

        sendMessage.setText("Вы успешно авторизованы");
        UserTelegram userTelegram = new UserTelegram();
        userTelegram.setUser(user.get());
        userTelegram.setChatId(chatId);
        userTelegramRepository.save(userTelegram);
        chatStates.put(chatId, UserState.WORK);
        }
        else{
            sendMessage.setText("Неверный пароль");
        }
        sender.execute(sendMessage);
    }
    private void messageHandler(Long chatId,Message message){
        if(message.getText().equals("Log out")){
            chatStates.put(chatId, UserState.START);
            SendMessage sendMessage = new SendMessage();
            sendMessage.setChatId(chatId);
            userTelegramRepository.deleteByChatId(chatId);
            sendMessage.setText("Вы вышли из аккаунта");
            sender.execute(sendMessage);
        }
    }
    public boolean userIsActive(Long chatId) {
        return chatStates.containsKey(chatId);
    }

}