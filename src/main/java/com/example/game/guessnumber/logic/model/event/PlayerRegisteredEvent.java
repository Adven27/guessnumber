package com.example.game.guessnumber.logic.model.event;

import static com.example.game.guessnumber.logic.model.event.GameEventType.PLAYER_REGISTERED;

public class PlayerRegisteredEvent extends PlayerEvent {
    public PlayerRegisteredEvent(String user) {
        super(PLAYER_REGISTERED, user);
    }
}
