# Placing a bet

## Overview

After a start of the game round player can place a bet by sending a message with the type `BET`:
```json
{"type": "BET", "bet": 10.0, "number": 7 }
```

## Examples

### Before each

#### [ ](- "before")

[There is no logged in players.](- "logoutAll()")

### Bet confirmation

Player receives bet acceptance event from the server after he has placed a bet.

#### [Bet confirmation](-)

__Given__

---

Player [Adam](- "startRoundFor(#TEXT)") starts a round. 

__When__

---

- [Adam places a bet:][adam sends]
-   ```json
    {"type": "BET", "bet": 10.0, "number": 1 }
    ```

__Then__

---

- [Player receive bet confirmation event][adam receives]
-   ```json
    {"type": "BET_PLACED", "player" : "Adam" }
    ```
-   ```json
    {"type":"COUNTDOWN","value":1}
    ```
-   ```json
    {"type": "ROUND_RESULT", "value" : 7 }
    ```
-   ```json
    {"type": "PLAYER_RESULT", "won" : false }
    ```
-   ```json
    {"type": "RESULTS", "winners" : [] }
    ```
  
### Bet validation

Bet amount and number should be valid.

#### [Bet validation](-)

__Given__

---

Player [Adam](- "startRoundFor(#TEXT)") starts a round.

__When__

---

- [Adam places an invalid bets:][adam sends]
-   ```json
    {"type": "BET", "bet": 0.0, "number": 100 }
    ```
-   ```json
    {"type": "BET" }
    ```       

__Then__

---

- [Adam receives errors][adam receives]
-   ```json
    {"type":"ERROR","violations":{"bet":"must be greater than 0.1","number":"must be less than or equal to 10"}}
    ```
-   ```json
    {"type":"ERROR","violations":{"bet":"must not be null","number":"must not be null"}}
    ```
-   ```json
    {"type":"COUNTDOWN","value":1}
    ```
-   ```json
    {"type":"ROUND_RESULT", "value":7}
    ```
-   ```json
    {"type":"PLAYER_RESULT", "won":false}
    ```  
-   ```json
    {"type":"RESULTS", "winners":[]}
    ```  

### Bet acceptance

Only bets placed during a round are accepted. Otherwise, server sends an error.

#### [Bet before round start](-)

__Given__

---

Player [Adam](- "login(#TEXT)") logins.

__When__

---

- [Adam places a bet:][adam sends]
-   ```json
    {"type": "BET", "bet": 1.0, "number": 1 }
    ```

__Then__

---

- [Adam receives errors][adam receives]
-   ```json
    {"type":"ERROR","violations":{"error":"Round not started"}}
    ```

[adam sends]: - "e:mq-send=Adam"
[adam receives]: - "e:mq-check=Adam awaitAtMostSec=2"