package com.example.game.guessnumber.logic.model.event;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import static com.example.game.guessnumber.logic.model.event.GameEventType.RESULTS;

public class ResultsEvent extends GameEvent {
    private final List<Winner> winners;

    public ResultsEvent(List<Winner> winners) {
        super(RESULTS);
        this.winners = winners;
    }

    public List<Winner> getWinners() {
        return winners;
    }

    @Override
    public String toString() {
        return "ResultsEvent{winners=" + winners + '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ResultsEvent)) return false;
        if (!super.equals(o)) return false;
        ResultsEvent that = (ResultsEvent) o;
        return winners.equals(that.winners);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), winners);
    }

    public static class Winner {
        private final String name;
        private final BigDecimal win;

        public Winner(String name, BigDecimal win) {
            this.name = name;
            this.win = win;
        }

        public String getName() {
            return name;
        }

        public BigDecimal getWin() {
            return win;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (!(o instanceof Winner)) return false;
            Winner winner = (Winner) o;
            return name.equals(winner.name) && win.equals(winner.win);
        }

        @Override
        public int hashCode() {
            return Objects.hash(name, win);
        }

        @Override
        public String toString() {
            return "Winner{name='" + name + '\'' + ", win=" + win + '}';
        }
    }
}
