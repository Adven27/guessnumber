package com.example.game.guessnumber.logic.model.action;

import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.Digits;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.util.Objects;

public class PlayerBetAction extends Action {
    public static final String TYPE = "BET";

    @NotNull
    @DecimalMin(value = "0.1", inclusive = false)
    @Digits(integer = 3, fraction = 2)
    private final BigDecimal bet;

    @NotNull
    @Min(1)
    @Max(10)
    private final Integer number;

    public PlayerBetAction(BigDecimal bet, Integer number) {
        super(TYPE);
        this.bet = bet;
        this.number = number;
    }

    public BigDecimal getBet() {
        return bet;
    }

    public Integer getNumber() {
        return number;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof PlayerBetAction)) return false;
        if (!super.equals(o)) return false;
        PlayerBetAction that = (PlayerBetAction) o;
        return bet.equals(that.bet) && number.equals(that.number);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), bet, number);
    }

    @Override
    public String toString() {
        return "PlayerBetAction{bet=" + bet + ", number=" + number + '}';
    }
}
