# Single player

## Overview

Game process:

1. Player registers in the game
2. Player sends a `READY` message to the server 
3. The server starts a round of the game and gives `guess-number.countdown` * `guess-number.stepMillis` seconds to place a bet from the players on numbers from 1 to 10 with the amount of the bet 
4. After the time expires, the server generates a random number from 1 to 10 
5. If the player guesses the number, a message is sent to him that he won with a winnings of 9.9 times the stake 
6. If the player loses receives a message about the loss 
7. All players receive a message with a list of winning players in which there is a nickname and the amount of winnings

## Examples

### Before each

#### [ ](- "before")

[There is no logged in players.](- "logoutAll()")

### Win

If the player guesses the number, a message is sent to him, that he won with a winnings of 9.9 times the stake.
Also, player receives a message with a list of winning players in which there is a nickname and the amount of winnings.

#### [Win](-)

__When__

---

A player:

1. logins as `Adam`
2. starts a game
3. places a bet of `10.0` on number `7`

- [Adam sends]
-   ```json
    {"type": "LOGIN", "player" : "Adam" }
    ```
-   ```json
    {"type": "READY"}
    ```
-   ```json
    {"type": "BET", "bet": 10.0, "number": 7 }
    ```

__Then__

---

Server sends to player status events and game events.

- [Adam receives]
-   ```json
    {"type":"CONNECTED", "players":["Adam"]}
    ```
-   ```json
    {"type": "PLAYER_READY", "player" : "Adam" }
    ```
-   ```json
    {"type": "COUNTDOWN", "value" : 2 }
    ```
-   ```json
    {"type": "BET_PLACED", "player" : "Adam" }
    ```    
-   ```json
    {"type": "COUNTDOWN", "value" : 1 }
    ```
-   ```json
    {"type": "ROUND_RESULT", "value" : 7 }
    ```
-   ```json
    {"type": "PLAYER_RESULT", "won" : true, "amount": 99.00 }
    ```  
-   ```json
    {"type": "RESULTS", "winners" : [ { "name": "Adam", "win" : 99.00} ] }
    ```

### Lose

If the player loses he receives a message about the loss.
Also, player receives a message with an empty list of winning players.

#### [Lose](-)

__When__

---

- [Adam sends]
-   ```json
    {"type": "LOGIN", "player" : "Adam" }
    ```
-   ```json
    {"type": "READY"}
    ```
-   ```json
    {"type": "BET", "bet": 10.0, "number": 2 }
    ```

__Then__

---

- [Adam receives in any order]
-   ```json
    {"type": "CONNECTED", "players":["Adam"]}
    ```
-   ```json
    {"type": "PLAYER_READY", "player" : "Adam" }
    ```
-   ```json
    {"type": "BET_PLACED", "player" : "Adam" }
    ```    
-   ```json
    {"type": "COUNTDOWN", "value" : 2 }
    ```
-   ```json
    {"type": "COUNTDOWN", "value" : 1 }
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
#### ~~Lose~~

Absent of a bet is considered as a lose.

#### [No bet placed](-)

__When__

- [Adam sends]
-   ```json
    {"type": "LOGIN", "player" : "Adam" }
    ```
-   ```json
    {"type": "READY"}
    ```

__Then__

- [Adam receives]
-   ```json
    {"type": "CONNECTED", "players":["Adam"]}
    ```
-   ```json
    {"type": "PLAYER_READY", "player" : "Adam" }
    ```
-   ```json
    {"type": "COUNTDOWN", "value" : 2 }
    ```
-   ```json
    {"type": "COUNTDOWN", "value" : 1 }
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

[Adam sends]: - "e:mq-send=Adam"
[Adam receives]: - "e:mq-check=Adam awaitAtMostSec=3"
[Adam receives in any order]: - "e:mq-check=Adam awaitAtMostSec=3 contains=any-order"