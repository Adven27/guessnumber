package com.example.game.guessnumber.logic.model.event;

import java.util.Map;
import java.util.Objects;

import static com.example.game.guessnumber.logic.model.event.GameEventType.ERROR;

public class ErrorEvent extends GameEvent {
    private final Map<String, String> violations;

    public ErrorEvent(Map<String, String> violations) {
        super(ERROR);
        this.violations = violations;
    }

    public Map<String, String> getViolations() {
        return violations;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ErrorEvent)) return false;
        if (!super.equals(o)) return false;
        ErrorEvent that = (ErrorEvent) o;
        return violations.equals(that.violations);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), violations);
    }

    @Override
    public String toString() {
        return "ErrorEvent{violations=" + violations + "} ";
    }

    public static class Violation {
        private final String path;
        private final String message;

        public Violation(String path, String message) {
            this.path = path;
            this.message = message;
        }

        public String getMessage() {
            return message;
        }

        public String getPath() {
            return path;
        }

        @Override
        public String toString() {
            return "Violation{path='" + path + "', message='" + message + "'}";
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (!(o instanceof Violation)) return false;
            Violation violation = (Violation) o;
            return path.equals(violation.path) && message.equals(violation.message);
        }

        @Override
        public int hashCode() {
            return Objects.hash(path, message);
        }
    }
}
