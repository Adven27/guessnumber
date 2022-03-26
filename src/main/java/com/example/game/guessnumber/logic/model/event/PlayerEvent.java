package com.example.game.guessnumber.logic.model.event;

import java.util.Objects;

public class PlayerEvent extends GameEvent {
    private final String player;

    public PlayerEvent(GameEventType type, String player) {
        super(type);
        this.player = player;
    }

    public String getPlayer() {
        return player;
    }

    @Override
    public String toString() {
        return "PlayerEvent{player='" + player + "', type='" + getType() + "'}";
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof PlayerEvent)) return false;
        if (!super.equals(o)) return false;
        PlayerEvent that = (PlayerEvent) o;
        return Objects.equals(player, that.player);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), player);
    }
}
