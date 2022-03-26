package com.example.game.guessnumber.logic.model.event;

public class PlayerReadyEvent extends PlayerEvent {
    public PlayerReadyEvent(String player) {
        super(GameEventType.PLAYER_READY, player);
    }
}
