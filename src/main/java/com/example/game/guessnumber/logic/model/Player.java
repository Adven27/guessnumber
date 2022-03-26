package com.example.game.guessnumber.logic.model;

import com.example.game.guessnumber.logic.model.event.GameEvent;

import java.math.BigDecimal;
import java.util.Objects;

public abstract class Player {
    private final String name;
    private boolean ready;
    private Integer number;
    private BigDecimal bet;
    private BigDecimal win;
    private BigDecimal balance;

    public Player(String name) {
        this.name = name;
    }

    public abstract void notify(GameEvent event);

    public BigDecimal getBalance() {
        return balance;
    }

    public void setBalance(BigDecimal balance) {
        this.balance = balance;
    }

    public BigDecimal getBet() {
        return bet;
    }

    public void setBet(BigDecimal bet) {
        this.bet = bet;
    }

    public Boolean isReady() {
        return ready;
    }

    public void setReady(Boolean ready) {
        this.ready = ready;
    }

    public String getName() {
        return name;
    }

    public Integer getNumber() {
        return number;
    }

    public void setNumber(Integer number) {
        this.number = number;
    }

    public void placeBet(BigDecimal bet, Integer number) {
       setBet(bet);
       setNumber(number);
    }

    public BigDecimal getWin() {
        return win;
    }

    public Boolean isWon() {
        return win != null;
    }

    public Player setWin(BigDecimal win) {
        this.win = win;
        return this;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Player player = (Player) o;
        return name.equals(player.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name);
    }
}
