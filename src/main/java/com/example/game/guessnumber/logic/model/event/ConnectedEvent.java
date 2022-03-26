package com.example.game.guessnumber.logic.model.event;

import java.util.Objects;
import java.util.Set;

import static com.example.game.guessnumber.logic.model.event.GameEventType.CONNECTED;

public class ConnectedEvent extends GameEvent {
    private final Set<String> players;

    public ConnectedEvent(Set<String> players) {
        super(CONNECTED);
        this.players = players;
    }

    public Set<String> getPlayers() {
        return players;
    }

    @Override
    public String toString() {
        return "ConnectedEvent{players=" + players + '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ConnectedEvent)) return false;
        if (!super.equals(o)) return false;
        ConnectedEvent that = (ConnectedEvent) o;
        return players.equals(that.players);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), players);
    }
}
