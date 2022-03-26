package specs;

import com.example.game.guessnumber.logic.model.event.GameEventType;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.github.adven27.concordion.extensions.exam.mq.MqTester;
import org.awaitility.core.ConditionFactory;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.client.WebSocketClient;
import org.springframework.web.socket.client.standard.StandardWebSocketClient;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentLinkedDeque;
import java.util.stream.Collectors;

import static java.time.Duration.ofMillis;
import static java.util.stream.Collectors.toList;
import static org.awaitility.Awaitility.await;
import static specs.WebSocketTester.State.BET_PLACED;
import static specs.WebSocketTester.State.READY;
import static specs.WebSocketTester.State.REGISTERED;
import static specs.WebSocketTester.State.UNREGISTERED;

class WebSocketTester implements MqTester {
    private static final Logger LOGGER = LoggerFactory.getLogger(WebSocketTester.class);
    private static final ConditionFactory AWAIT = await().pollInterval(ofMillis(50));
    private static final WebSocketClient CLIENT = new StandardWebSocketClient();
    private static final ObjectMapper MAPPER = new ObjectMapper();

    private final ConcurrentLinkedDeque<Message> queue = new ConcurrentLinkedDeque<>();
    private final String uri;
    private final String name;
    private WebSocketSession session;
    private State state = State.UNREGISTERED;
    private boolean playing;

    WebSocketTester(String name, String uri) {
        this.name = name;
        this.uri = uri;
    }

    @Override
    public boolean accumulateOnRetries() {
        return true;
    }

    @Override
    public void purge() {
        queue.clear();
    }

    @Override
    public @NotNull List<Message> receive() {
        return queue.stream().map((m) -> queue.poll()).collect(toList());
    }

    @Override
    public void send(@NotNull MqTester.Message message, @NotNull Map<String, String> params) {
        try {
            session.sendMessage(new TextMessage(message.getBody()));
        } catch (Exception e) {
            LOGGER.error("Failed to send", e);
        }
    }

    @Override
    public void start() {
    }

    public void connect() {
        try {
            session = CLIENT.doHandshake(
                new TextWebSocketHandler() {
                    @Override
                    public void handleTextMessage(WebSocketSession session, TextMessage message) {
                        LOGGER.info("Tester received: {}", message.getPayload());
                        queue.add(new MqTester.Message(message.getPayload()));
                        updateState(message);
                    }
                },
                uri
            ).get();
        } catch (Exception e) {
            LOGGER.error("Failed to start", e);
        }
    }

    private void updateState(TextMessage message) {
        try {
            final Map<String, String> event = MAPPER.readValue(message.getPayload(), Map.class);
            switch (GameEventType.valueOf(event.get("type"))) {
                case CONNECTED:
                    state =  REGISTERED;
                    break;
                case PLAYER_READY:
                    state = isMine(event) ? READY : state;
                    break;
                case BET_PLACED:
                    state = isMine(event) ? BET_PLACED : state;
                    break;
                case PLAYER_UNREGISTERED:
                    state = isMine(event) ? UNREGISTERED: state;
                    break;
                case COUNTDOWN:
                    playing = true;
                    break;
                case RESULTS:
                    playing = false;
                    state = REGISTERED;
                    break;
            }
        } catch (JsonProcessingException e) {
            LOGGER.error("Unsupported game event", e);
        }
    }

    private boolean isMine(Map<String, String> event) {
        return name.equals(event.get("player"));
    }

    @Override
    public void stop() {
        try {
            session.close();
        } catch (Exception e) {
            LOGGER.error("Failed to stop", e);
        }
    }

    public void login() {
        send(new Message("{\"type\": \"LOGIN\", \"player\": \"" + name + "\"}"), Map.of());
        AWAIT.until(() -> state == REGISTERED);
    }

    public void ready() {
        send(new Message("{\"type\": \"READY\"}"), Map.of());
        AWAIT.until(() -> state == READY);
    }

    public void logoutAll() {
        send(new Message("{\"type\": \"LOGOUT_ALL\"}"), Map.of());
        AWAIT.until(() -> state == UNREGISTERED);
    }

    public void playing() {
        AWAIT.until(() -> playing);
    }

    enum State {UNREGISTERED, REGISTERED, READY, BET_PLACED}
}
