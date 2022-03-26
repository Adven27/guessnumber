package com.example.game.guessnumber.logic;

import com.example.game.guessnumber.logic.model.Player;
import com.example.game.guessnumber.logic.model.event.ConnectedEvent;
import com.example.game.guessnumber.logic.model.event.CountdownEvent;
import com.example.game.guessnumber.logic.model.event.GameEvent;
import com.example.game.guessnumber.logic.model.event.PlayerLoseEvent;
import com.example.game.guessnumber.logic.model.event.PlayerPlacedBetEvent;
import com.example.game.guessnumber.logic.model.event.PlayerReadyEvent;
import com.example.game.guessnumber.logic.model.event.PlayerRegisteredEvent;
import com.example.game.guessnumber.logic.model.event.PlayerUnregisteredEvent;
import com.example.game.guessnumber.logic.model.event.PlayerWonEvent;
import com.example.game.guessnumber.logic.model.event.ResultsEvent;
import com.example.game.guessnumber.logic.model.event.RoundResultEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.math.BigDecimal;
import java.util.Collection;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Consumer;

import static java.util.stream.Collectors.toList;
import static java.util.stream.IntStream.range;

public class Game {
    private static final Logger LOGGER = LoggerFactory.getLogger(Game.class);
    private static final BigDecimal COEF = BigDecimal.valueOf(9.9);

    private final ConcurrentHashMap<String, Player> players = new ConcurrentHashMap<>();
    private final int countdown;
    private final ResultGenerator resultGenerator;
    private final long stepMillis;
    private CompletableFuture<Void> future;

    public Game(ResultGenerator resultGenerator, int countdown, long stepMillis) {
        this.resultGenerator = resultGenerator;
        this.countdown = countdown;
        this.stepMillis = stepMillis;
    }

    public Game(ResultGenerator resultGenerator) {
        this.resultGenerator = resultGenerator;
        this.countdown = 10;
        this.stepMillis = 1000;
    }

    public Collection<Player> getPlayers() {
        return players.values();
    }

    public void registerPlayer(Player player) {
        notifyAllPlayers(new PlayerRegisteredEvent(player.getName()));
        players.put(player.getName(), player);
        player.notify(new ConnectedEvent(players.keySet()));
    }

    public void unregisterPlayer(String player) {
        notifyAllPlayers(new PlayerUnregisteredEvent(player));
        players.remove(player);
    }

    public void playerReady(String player) {
        if (pendingRound()) {
            throw new RoundInProgressException();
        } else {
            players.get(player).setReady(true);
            notifyAllPlayers(new PlayerReadyEvent(player));
            if (allPlayersReady()) {
                playRound();
            }
        }
    }

    public void placeBet(String player, BigDecimal bet, int number) {
        if (pendingRound()) {
            players.get(player).placeBet(bet, number);
            notifyAllPlayers(new PlayerPlacedBetEvent(player));
        } else {
            throw new RoundNotStartedException();
        }
    }

    private boolean pendingRound() {
        return future != null && !future.isDone();
    }

    private void playRound() {
        future = CompletableFuture.supplyAsync(this::betPhase).thenAccept(this::settlementPhase);
    }

    private Integer betPhase() {
        range(0, countdown).forEach(this::countdown);
        Integer number = resultGenerator.generate();
        notifyAllPlayers(new RoundResultEvent(number));
        return number;
    }

    private void notifyAllPlayers(GameEvent event) {
        notifyEach(player -> player.notify(event));
    }

    private void notifyEach(Consumer<Player> action) {
        players.values().forEach(action);
    }

    private void settlementPhase(Integer result) {
        notifyAllPlayers(
            new ResultsEvent(
                players.values().stream()
                    .map(p -> p.setWin(result.equals(p.getNumber()) ? calcWin(p) : null))
                    .peek(this::notifyPlayerResult)
                    .filter(Player::isWon)
                    .map(p -> new ResultsEvent.Winner(p.getName(), p.getWin()))
                    .collect(toList())
            )
        );
        players.forEach((s, p) -> p.setReady(false));
    }

    private void notifyPlayerResult(Player player) {
        player.notify(player.isWon() ? new PlayerWonEvent(player.getWin()) : new PlayerLoseEvent());
    }

    private BigDecimal calcWin(Player p) {
        return p.getBet().multiply(COEF);
    }

    private void countdown(int n) {
        try {
            LOGGER.debug("Countdown {}", countdown - n);
            notifyAllPlayers(new CountdownEvent(countdown - n));
            Thread.sleep(stepMillis);
        } catch (Exception e) {
            LOGGER.error("Countdown failed", e);
        }
    }

    private boolean allPlayersReady() {
        return players.values().stream().allMatch(Player::isReady);
    }

    public static class RoundNotStartedException extends IllegalStateException {
        public RoundNotStartedException() {
            super("Round not started");
        }
    }

    public static class RoundInProgressException extends IllegalStateException {
        public RoundInProgressException() {
            super("Round in progress");
        }
    }
}
