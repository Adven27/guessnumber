package com.example.game.guessnumber;

import com.example.game.guessnumber.logic.Game;
import com.example.game.guessnumber.logic.ResultGeneratorType;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.socket.config.annotation.EnableWebSocket;
import org.springframework.web.socket.config.annotation.WebSocketConfigurer;
import org.springframework.web.socket.config.annotation.WebSocketHandlerRegistry;

@SpringBootApplication
@EnableWebSocket
public class GuessnumberApplication implements WebSocketConfigurer {
    private final MessageHandler handler;

    public GuessnumberApplication(MessageHandler handler) {
        this.handler = handler;
    }

    @Override
    public void registerWebSocketHandlers(WebSocketHandlerRegistry registry) {
        registry.addHandler(handler, "/web-socket");
    }

    public static void main(String[] args) {
        SpringApplication.run(GuessnumberApplication.class, args);
    }
}

@Configuration
class Config {

    @Bean
    public Game game(
        @Value("${guess-number.countdown:10}") int countdown,
        @Value("${guess-number.stepMillis:1000}") int stepMillis,
        @Value("${guess-number.result-generator:random}") String generator
    ) {
        return new Game(ResultGeneratorType.valueOf(generator.toUpperCase()).getGenerator(), countdown, stepMillis);
    }
}
