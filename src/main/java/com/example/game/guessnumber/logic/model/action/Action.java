package com.example.game.guessnumber.logic.model.action;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;

import javax.validation.constraints.NotBlank;
import java.util.Objects;

import static com.fasterxml.jackson.annotation.JsonTypeInfo.As.EXISTING_PROPERTY;
import static com.fasterxml.jackson.annotation.JsonTypeInfo.Id.NAME;

@JsonTypeInfo(use = NAME, include = EXISTING_PROPERTY, property = "type")
@JsonSubTypes({
    @JsonSubTypes.Type(value = PlayerLoginAction.class, name = PlayerLoginAction.TYPE),
    @JsonSubTypes.Type(value = PlayerReadyAction.class, name = PlayerReadyAction.TYPE),
    @JsonSubTypes.Type(value = PlayerBetAction.class, name = PlayerBetAction.TYPE),
    @JsonSubTypes.Type(value = LogoutAction.class, name = LogoutAction.TYPE),
    @JsonSubTypes.Type(value = LogoutAllAction.class, name = LogoutAllAction.TYPE)
})
public class Action {
    @NotBlank
    private final String type;

    public Action(String type) {
        this.type = type;
    }

    public String getType() {
        return type;
    }

    @Override
    public String toString() {
        return "Action{type='" + type + '\'' + '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Action)) return false;
        Action action = (Action) o;
        return type.equals(action.type);
    }

    @Override
    public int hashCode() {
        return Objects.hash(type);
    }
}
