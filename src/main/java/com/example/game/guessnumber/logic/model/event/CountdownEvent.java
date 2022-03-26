package com.example.game.guessnumber.logic.model.event;

import java.util.Objects;

public class CountdownEvent extends GameEvent {
    private final Integer value;

    public CountdownEvent(Integer value) {
        super(GameEventType.COUNTDOWN);
        this.value = value;
    }

    public Integer getValue() {
        return value;
    }

    @Override
    public String toString() {
        return "CountdownEvent{value=" + value + '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof CountdownEvent)) return false;
        if (!super.equals(o)) return false;
        CountdownEvent that = (CountdownEvent) o;
        return value.equals(that.value);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), value);
    }
}
