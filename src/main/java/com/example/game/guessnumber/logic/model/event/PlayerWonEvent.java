package com.example.game.guessnumber.logic.model.event;

import java.math.BigDecimal;
import java.util.Objects;

public class PlayerWonEvent extends PlayerResultEvent {
    private final BigDecimal amount;

    public PlayerWonEvent(BigDecimal amount) {
        super(true);
        this.amount = amount;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    @Override
    public String toString() {
        return "PlayerWonEvent{amount=" + amount + '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof PlayerWonEvent)) return false;
        if (!super.equals(o)) return false;
        PlayerWonEvent that = (PlayerWonEvent) o;
        return amount.equals(that.amount);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), amount);
    }
}
