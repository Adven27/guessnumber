package com.example.game.guessnumber;

import com.example.game.guessnumber.logic.Game;
import com.example.game.guessnumber.logic.model.Player;
import com.example.game.guessnumber.logic.model.event.ConnectedEvent;
import com.example.game.guessnumber.logic.model.event.CountdownEvent;
import com.example.game.guessnumber.logic.model.event.GameEvent;
import com.example.game.guessnumber.logic.model.event.PlayerLoseEvent;
import com.example.game.guessnumber.logic.model.event.PlayerPlacedBetEvent;
import com.example.game.guessnumber.logic.model.event.PlayerReadyEvent;
import com.example.game.guessnumber.logic.model.event.PlayerRegisteredEvent;
import com.example.game.guessnumber.logic.model.event.PlayerWonEvent;
import com.example.game.guessnumber.logic.model.event.ResultsEvent;
import com.example.game.guessnumber.logic.model.event.RoundResultEvent;
import org.awaitility.core.ConditionFactory;
import org.junit.Test;

import java.math.BigDecimal;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import static java.lang.System.currentTimeMillis;
import static java.math.BigDecimal.ONE;
import static java.math.BigDecimal.TEN;
import static java.time.Duration.ofMillis;
import static java.time.Duration.ofSeconds;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatExceptionOfType;
import static org.awaitility.Awaitility.await;

public class GameTest {
    private static final int COUNTDOWN = 2;
    public static final int STEP_MILLIS = 100;
    private static final long ROUND_DURATION_MILLIS = COUNTDOWN * STEP_MILLIS;
    private static final int RESULT = 2;
    private static final GameEvent[] ROUND_EVENTS = {
        new CountdownEvent(2),
        new CountdownEvent(1),
        new RoundResultEvent(RESULT)
    };
    private final TestPlayer playerA = new TestPlayer("Adam");
    private final TestPlayer playerB = new TestPlayer("Bob");
    private final Game sut = new Game(() -> RESULT, COUNTDOWN, STEP_MILLIS);

    @Test
    public void canRegisterPlayer() {
        sut.registerPlayer(playerA);

        assertThat(sut.getPlayers()).containsExactly(playerA);
    }

    @Test
    public void shouldNotifyPlayerAboutRegistration() {
        sut.registerPlayer(playerA);

        assertThat(playerA.notifications()).containsExactly(new ConnectedEvent(Set.of(playerA.getName())));
    }

    @Test
    public void shouldNotifyAllPlayersAboutNewRegistration() {
        sut.registerPlayer(playerA);
        sut.registerPlayer(playerB);

        assertThat(playerA.notifications()).containsExactly(
            new ConnectedEvent(Set.of(playerA.getName(), playerB.getName())),
            new PlayerRegisteredEvent(playerB.getName())
        );

        assertThat(playerB.notifications()).containsExactly(
            new ConnectedEvent(Set.of(playerA.getName(), playerB.getName()))
        );
    }

    @Test
    public void canAcceptPlayerReadiness() {
        playerA.setReady(false);
        sut.registerPlayer(playerA);

        sut.playerReady(playerA.getName());

        assertThat(playerA.isReady()).isEqualTo(true);
    }

    @Test
    public void failToAcceptNotRegisteredPlayerReadiness() {
        assertThatExceptionOfType(RuntimeException.class).isThrownBy(() -> sut.playerReady(playerA.getName()));
    }

    @Test
    public void shouldNotifyPlayerAboutReadinessAcceptance() {
        sut.registerPlayer(playerA);
        playerA.cleanNotifications();

        sut.playerReady(playerA.getName());

        assertThat(playerA.notifications()).containsExactly(new PlayerReadyEvent(playerA.getName()));
    }

    @Test
    public void shouldNotifyAllPlayersAboutPlayerReadiness() {
        sut.registerPlayer(playerA);
        sut.registerPlayer(playerB);
        playerA.cleanNotifications();

        sut.playerReady(playerB.getName());

        assertThat(playerA.notifications()).containsExactly(new PlayerReadyEvent(playerB.getName()));
    }

    @Test
    public void shouldStartRoundWhenAllPlayersReady_singlePlayer() {
        sut.registerPlayer(playerA);

        sut.playerReady(playerA.getName());

        await("Round countdown started").atMost(ofSeconds(1)).untilAsserted(() ->
            assertThat(playerA.notifications()).contains(new CountdownEvent(COUNTDOWN))
        );
    }

    @Test
    public void shouldStartRoundWhenAllPlayersReady_multiPlayer() {
        sut.registerPlayer(playerA);
        sut.registerPlayer(playerB);

        sut.playerReady(playerA.getName());
        sut.playerReady(playerB.getName());

        await("Round countdown started").atMost(ofSeconds(1)).untilAsserted(() -> {
                assertThat(playerA.notifications()).contains(new CountdownEvent(COUNTDOWN));
                assertThat(playerB.notifications()).contains(new CountdownEvent(COUNTDOWN));
            }
        );
    }

    @Test
    public void canAcceptPlayerBet() {
        playerA.setNumber(null);
        playerA.setWin(null);
        sut.registerPlayer(playerA);
        sut.playerReady(playerA.getName());

        sut.placeBet(playerA.getName(), TEN, 2);

        assertThat(playerA).extracting("bet", "number").containsExactly(TEN, 2);
    }

    @Test
    public void failToAcceptNotRegisteredPlayerBet() {
        assertThatExceptionOfType(RuntimeException.class).isThrownBy(() -> sut.placeBet(playerA.getName(), TEN, 2));
    }

    @Test
    public void failToAcceptBetIfRoundNotStarted() {
        sut.registerPlayer(playerA);
        sut.registerPlayer(playerB);
        sut.playerReady(playerA.getName());

        assertThatExceptionOfType(RuntimeException.class).isThrownBy(() -> sut.placeBet(playerA.getName(), TEN, 2));
    }

    @Test
    public void shouldNotifyPlayerAboutBetAcceptance() {
        sut.registerPlayer(playerA);
        sut.playerReady(playerA.getName());
        playerA.cleanNotifications();

        sut.placeBet(playerA.getName(), TEN, 2);

        assertThat(playerA.notifications()).containsExactly(new PlayerPlacedBetEvent(playerA.getName()));
    }

    @Test
    public void shouldNotifyAllPlayersAboutPlayerBet() {
        sut.registerPlayer(playerA);
        sut.registerPlayer(playerB);
        sut.playerReady(playerA.getName());
        sut.playerReady(playerB.getName());
        playerA.cleanNotifications();

        sut.placeBet(playerB.getName(), TEN, 2);

        assertThat(playerA.notifications()).containsExactly(new PlayerPlacedBetEvent(playerB.getName()));
    }

    @Test
    public void givenRoundStarted_shouldNotifyAllPlayersAboutRoundResult() {
        sut.registerPlayer(playerA);
        sut.registerPlayer(playerB);
        playerA.cleanNotifications();
        playerB.cleanNotifications();

        sut.playerReady(playerA.getName());
        sut.playerReady(playerB.getName());

        awaitRoundComplete().untilAsserted(() -> {
                assertThat(playerA.notifications()).contains(ROUND_EVENTS);
                assertThat(playerB.notifications()).contains(ROUND_EVENTS);
            }
        );
        assertThat(playerA.periodBetween(new RoundResultEvent(RESULT), new CountdownEvent(COUNTDOWN)))
            .isBetween(ROUND_DURATION_MILLIS, ROUND_DURATION_MILLIS + 500);
        assertThat(playerB.periodBetween(new RoundResultEvent(RESULT), new CountdownEvent(COUNTDOWN)))
            .isBetween(ROUND_DURATION_MILLIS, ROUND_DURATION_MILLIS + 500);
    }

    @Test
    public void shouldNotifyPlayerAboutLose() {
        sut.registerPlayer(playerA);
        sut.playerReady(playerA.getName());

        sut.placeBet(playerA.getName(), ONE, RESULT + 1);

        awaitRoundComplete().untilAsserted(
            () -> assertThat(playerA.notifications()).contains(new PlayerLoseEvent())
        );
    }

    @Test
    public void shouldNotifyPlayerAboutWin() {
        sut.registerPlayer(playerA);
        sut.playerReady(playerA.getName());

        sut.placeBet(playerA.getName(), ONE, RESULT);

        awaitRoundComplete().untilAsserted(
            () -> assertThat(playerA.notifications()).contains(new PlayerWonEvent(BigDecimal.valueOf(9.9)))
        );
    }

    @Test
    public void shouldNotifyAllPlayersAboutWinners() {
        sut.registerPlayer(playerA);
        sut.registerPlayer(playerB);
        sut.playerReady(playerA.getName());
        sut.playerReady(playerB.getName());

        sut.placeBet(playerA.getName(), ONE, RESULT);
        sut.placeBet(playerB.getName(), ONE, RESULT + 1);

        ResultsEvent expected = new ResultsEvent(
            List.of(new ResultsEvent.Winner(playerA.getName(), BigDecimal.valueOf(9.9)))
        );
        awaitRoundComplete().untilAsserted(() -> {
                assertThat(playerA.notifications()).contains(expected);
                assertThat(playerB.notifications()).contains(expected);
            }
        );
    }

    private ConditionFactory awaitRoundComplete() {
        return await("Round completed").pollDelay(ofMillis(ROUND_DURATION_MILLIS));
    }

    static class TestPlayer extends Player {
        private final Map<GameEvent, Long> events = new LinkedHashMap<>();

        public TestPlayer(String name) {
            super(name);
        }

        public Set<GameEvent> notifications() {
            return events.keySet();
        }

        public long periodBetween(GameEvent first, GameEvent second) {
            return events.get(first) - events.get(second);
        }

        public void cleanNotifications() {
            events.clear();
        }

        @Override
        public void notify(GameEvent event) {
            events.put(event, currentTimeMillis());
        }
    }
}
