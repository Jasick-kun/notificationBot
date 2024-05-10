package com.example.notificationbot.Controller;

import com.example.notificationbot.Repository.UserTelegramRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.telegram.abilitybots.api.bot.AbilityBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.util.List;
import java.util.Optional;

@Controller
@RequiredArgsConstructor
@RequestMapping(value = "/newgrade")
public class NewGradeController {

    @Autowired
    AbilityBot bot;

    private final UserTelegramRepository userTelegramRepository;

    @PostMapping("/")
    public ResponseEntity<Object> newGrade(@RequestBody List<Grade> request){

        for (Grade grade : request) {
            Optional<Long> chatId = userTelegramRepository.findChatIdByUsername(grade.username);
            if(chatId.isPresent()){
                SendMessage sendMessage = new SendMessage();
                sendMessage.setChatId(chatId.get());
                sendMessage.setText("Новая оценка по дисциплине "+grade.type+" "+grade.discipline+" = "+grade.grade+", неделя = "+grade.week);
                try {
                    bot.execute(sendMessage);
                } catch (TelegramApiException e) {
                    throw new RuntimeException(e);
                }
            }

        }
        return ResponseEntity.ok().build();
    }
}
