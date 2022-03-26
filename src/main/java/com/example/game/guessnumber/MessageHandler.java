package com.example.game.guessnumber;

import com.example.game.guessnumber.logic.Game;
import com.example.game.guessnumber.logic.model.Player;
import com.example.game.guessnumber.logic.model.action.Action;
import com.example.game.guessnumber.logic.model.action.ActionType;
import com.example.game.guessnumber.logic.model.action.PlayerBetAction;
import com.example.game.guessnumber.logic.model.action.PlayerLoginAction;
import com.example.game.guessnumber.logic.model.event.ErrorEvent;
import com.example.game.guessnumber.logic.model.event.GameEvent;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.ConcurrentWebSocketSessionDecorator;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import javax.validation.ConstraintViolation;
import javax.validation.Validator;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import static java.util.stream.Collectors.toMap;

@Component
public class MessageHandler extends TextWebSocketHandler {
    private static final Logger LOGGER = LoggerFactory.getLogger(MessageHandler.class);
    private final ConcurrentHashMap<String, String> sessionToPlayer = new ConcurrentHashMap<>();
    private final ObjectMapper mapper;
    private final Game game;
    private final Validator validator;

    public MessageHandler(ObjectMapper mapper, Game game, Validator validator) {
        this.mapper = mapper;
        this.game = game;
        this.validator = validator;
    }

    @Override
    public void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {
        LOGGER.info("Message received: {}", message.getPayload());
        final Action event = mapper.readValue(message.getPayload(), Action.class);
        final Set<ConstraintViolation<Action>> violations = validator.validate(event);

        if (violations.size() > 0) {
            LOGGER.warn("Message validation failed: {}", violations);
            send(
                new ErrorEvent(
                    violations.stream()
                        .collect(toMap(it -> it.getPropertyPath().toString(), ConstraintViolation::getMessage))
                ),
                session
            );
        } else {
            try {
                switch (ActionType.valueOf(event.getType())) {
                    case LOGIN:
                        login(session, (PlayerLoginAction) event);
                        break;
                    case READY:
                        game.playerReady(sessionToPlayer.get(session.getId()));
                        break;
                    case BET:
                        PlayerBetAction betAction = (PlayerBetAction) event;
                        game.placeBet(sessionToPlayer.get(session.getId()), betAction.getBet(), betAction.getNumber());
                        break;
                    case LOGOUT:
                        game.unregisterPlayer(sessionToPlayer.remove(session.getId()));
                        break;
                    case LOGOUT_ALL:
                        sessionToPlayer.keySet().forEach(id -> game.unregisterPlayer(sessionToPlayer.remove(id)));
                        break;
                }
            } catch (Exception e) {
                LOGGER.error("Message failed: {}", event, e);
                send(new ErrorEvent(Map.of("error", e.getMessage())), session);
            }
        }
    }

    private void login(WebSocketSession session, PlayerLoginAction event) {
        sessionToPlayer.put(session.getId(), event.getPlayer());
        game.registerPlayer(
            new Player(event.getPlayer()) {
                private final WebSocketSession s = concurrentSession(session);

                @Override
                public void notify(GameEvent event) {
                    send(event, s);
                }
            }
        );
    }

    private void send(GameEvent event, WebSocketSession ws) {
        try {
            LOGGER.info("Sending event {} to {}", event, sessionToPlayer.get(ws.getId()));
            ws.sendMessage(new TextMessage(mapper.writeValueAsString(event)));
        } catch (Exception e) {
            LOGGER.error("Failed to send message: {}", event, e);
        }
    }

    private WebSocketSession concurrentSession(WebSocketSession session) {
        return new ConcurrentWebSocketSessionDecorator(session, 2000, 1000);
    }
}
