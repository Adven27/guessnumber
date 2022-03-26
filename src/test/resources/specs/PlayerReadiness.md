# Players readiness confirmation

## Overview

In order to start a round player should confirm readiness by sending a message with the type `READY`:
```json
{"type": "READY" }
```

## Examples

### Before each

#### [ ](- "before")

[There is no logged in players.](- "logoutAll()")

### Readiness confirmation

Player receives readiness acceptance event from the server after he has sent readiness confirmation message.

#### [Readiness confirmation](-)

__Given__

---

Logged in players: [Adam, Bob](- "login(#TEXT)")

__When__

---

- [Adam sends a message:][adam sends]
-   ```json
    {"type": "READY" }
    ```

__Then__

---

- [Adam receives readiness confirmation event:][adam receives]
-   ```json
    {"type": "PLAYER_READY", "player" : "Adam" }
    ```

[adam sends]: - "e:mq-send=Adam"
[adam receives]: - "e:mq-check=Adam awaitAtMostSec=2 contains=any-order"
