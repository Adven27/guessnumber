package com.example.game.guessnumber.logic.model.event;

public class PlayerPlacedBetEvent extends PlayerEvent {
    public PlayerPlacedBetEvent(String user) {
        super(GameEventType.BET_PLACED, user);
    }
}
