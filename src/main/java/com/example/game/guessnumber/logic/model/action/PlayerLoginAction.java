package com.example.game.guessnumber.logic.model.action;

import javax.validation.constraints.NotBlank;
import java.util.Objects;

public class PlayerLoginAction extends Action {
    public static final String TYPE = "LOGIN";

    @NotBlank
    private String player;

    public PlayerLoginAction() {
        super(TYPE);
    }

    public String getPlayer() {
        return player;
    }

    public void setPlayer(String player) {
        this.player = player;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof PlayerLoginAction)) return false;
        if (!super.equals(o)) return false;
        PlayerLoginAction that = (PlayerLoginAction) o;
        return Objects.equals(player, that.player);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), player);
    }

    @Override
    public String toString() {
        return "PlayerLoginAction{player='" + player + "'}";
    }
}
