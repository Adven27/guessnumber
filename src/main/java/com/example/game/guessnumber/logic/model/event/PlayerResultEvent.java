package com.example.game.guessnumber.logic.model.event;

import java.util.Objects;

public class PlayerResultEvent extends GameEvent {
    private final Boolean won;

    public PlayerResultEvent(Boolean won) {
        super(GameEventType.PLAYER_RESULT);
        this.won = won;
    }

    public Boolean getWon() {
        return won;
    }

    @Override
    public String toString() {
        return "PlayerResultEvent{won=" + won + '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof PlayerResultEvent)) return false;
        if (!super.equals(o)) return false;
        PlayerResultEvent that = (PlayerResultEvent) o;
        return won.equals(that.won);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), won);
    }
}
