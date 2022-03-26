package com.example.game.guessnumber.logic.model.event;

import static com.example.game.guessnumber.logic.model.event.GameEventType.PLAYER_UNREGISTERED;

public class PlayerUnregisteredEvent extends PlayerEvent {
    public PlayerUnregisteredEvent(String user) {
        super(PLAYER_UNREGISTERED, user);
    }
}
