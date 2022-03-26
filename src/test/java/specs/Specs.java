package specs;

import com.example.game.guessnumber.GuessnumberApplication;
import io.github.adven27.concordion.extensions.exam.core.AbstractSpecs;
import io.github.adven27.concordion.extensions.exam.core.ExamExtension;
import io.github.adven27.concordion.extensions.exam.mq.MqPlugin;
import org.springframework.boot.SpringApplication;
import org.springframework.context.ConfigurableApplicationContext;

import java.util.Map;
import java.util.function.Consumer;

import static java.util.Arrays.asList;

public class Specs extends AbstractSpecs {
    public static final Map<String, WebSocketTester> TESTERS = Map.of(
        "Adam", new WebSocketTester("Adam", "ws://localhost:8080/web-socket"),
        "Bob", new WebSocketTester("Bob", "ws://localhost:8080/web-socket")
    );
    private static ConfigurableApplicationContext SUT;

    @Override
    protected ExamExtension init() {
        return new ExamExtension(new MqPlugin(TESTERS));
    }

    @Override
    protected void startSut() {
        SUT = new SpringApplication(GuessnumberApplication.class).run();
        TESTERS.forEach((name, tester) -> tester.connect());
    }

    @Override
    protected void stopSut() {
        SUT.stop();
    }

    public void login(String players) {
        onEach(players, WebSocketTester::login);
        TESTERS.values().forEach(WebSocketTester::purge);
    }

    public void startRoundFor(String players) {
        onEach(players, WebSocketTester::login);
        onEach(players, WebSocketTester::ready);
        onEach(players, WebSocketTester::playing);
        TESTERS.values().forEach(WebSocketTester::purge);
    }

    public void logoutAll() {
        TESTERS.values().iterator().next().logoutAll();
        TESTERS.values().forEach(WebSocketTester::purge);
    }

    private void onEach(String players, Consumer<WebSocketTester> consumer) {
        asList(players.split(",")).forEach(player -> {
            WebSocketTester tester = TESTERS.get(player.trim());
            consumer.accept(tester);
        });
    }
}
