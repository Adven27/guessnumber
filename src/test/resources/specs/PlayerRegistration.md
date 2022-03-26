# Player registration

## Overview

In order to connect to the game player should pass a registration by sending a message with the type `LOGIN`:
```json
{"type": "LOGIN", "player" : "<required player nickname>" }
```

## Examples

### Before each

#### [ ](- "before")

[There is no logged in players.](- "logoutAll()")

### Successful registration 

A player should provide a nickname to pass registration.

#### [Successful registration](-)

__When__

---

- [Player sends a message:][send]
-   ```json
    {"type": "LOGIN", "player" : "Adam" }
    ```

__Then__

---

- [Player receive successful registration event][receive]
-   ```json
    {"type": "CONNECTED", "players":["Adam"]}
    ```
#### ~~Successful registration~~

If nickname is not provided the server will respond with error message.

#### [Failed registration](-)

__When__

---

- [Player sends a message without nickname:][send]
-   ```json
    {"type": "LOGIN", "player" : "" }
    ```

__Then__

---

- [Player receives an error:][receive]
-   ```json
    {"type": "ERROR", "violations": {"player": "must not be blank"} } 
    ```

[send]: - "e:mq-send=Adam"
[receive]: - "e:mq-check=Adam awaitAtMostSec=2"