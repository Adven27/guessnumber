package com.example.game.guessnumber.logic.model.event;

import java.util.Objects;

import static com.example.game.guessnumber.logic.model.event.GameEventType.ROUND_RESULT;

public class RoundResultEvent extends GameEvent {
    private final Integer value;

    public RoundResultEvent(Integer value) {
        super(ROUND_RESULT);
        this.value = value;
    }

    public Integer getValue() {
        return value;
    }

    @Override
    public String toString() {
        return "RoundResultEvent{value=" + value + '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof RoundResultEvent)) return false;
        if (!super.equals(o)) return false;
        RoundResultEvent that = (RoundResultEvent) o;
        return value.equals(that.value);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), value);
    }
}
