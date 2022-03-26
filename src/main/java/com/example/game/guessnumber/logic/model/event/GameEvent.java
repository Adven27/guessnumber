package com.example.game.guessnumber.logic.model.event;

import java.util.Objects;

public class GameEvent {
    private final GameEventType type;

    public GameEvent(GameEventType type) {
        this.type = type;
    }

    public GameEventType getType() {
        return type;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof GameEvent)) return false;
        GameEvent gameEvent = (GameEvent) o;
        return type == gameEvent.type;
    }

    @Override
    public int hashCode() {
        return Objects.hash(type);
    }
}
