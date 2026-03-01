package com.bittasks.bittasks;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.updatesreceivers.DefaultBotSession;

@SpringBootApplication
public class BittasksApplication {

    public static void main(String[] args) {
        SpringApplication.run(BittasksApplication.class, args);
    }

    // Ele força o Spring a registrar e ligar o bot na API do Telegram.
    @Bean
    public TelegramBotsApi telegramBotsApi(BitTasksBot bot) throws TelegramApiException {
        TelegramBotsApi api = new TelegramBotsApi(DefaultBotSession.class);
        api.registerBot(bot);
        return api;
    }
}