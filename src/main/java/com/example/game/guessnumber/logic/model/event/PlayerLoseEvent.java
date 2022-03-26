package com.example.game.guessnumber.logic.model.event;

public class PlayerLoseEvent extends PlayerResultEvent {
    public PlayerLoseEvent() {
        super(false);
    }
}
