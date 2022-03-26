# Multiplayer

## Overview

Game process:

1. Players register in the game
2. Players send a `READY` message to the server 
3. __When all players are ready__ the server starts a round of the game and gives `guess-number.countdown` * `guess-number.stepMillis` seconds to place a bet from the players on numbers from 1 to 10 with the amount of the bet 
4. After the time expires, the server generates a random number from 1 to 10 
5. If the player guesses the number, a message is sent to him that he won with a winnings of 9.9 times the stake 
6. If the player loses receives a message about the loss 
7. All players receive a message with a list of winning players in which there is a nickname and the amount of winnings

## Examples

### Before each

#### [ ](- "before")

[There is no logged in players.](- "logoutAll()")

### Win and lose

If the player guesses the number, a message is sent to him, that he won with a winnings of 9.9 times the stake.
Also, player receives a message with a list of winning players in which there is a nickname and the amount of winnings.

#### [Adam wins, Bob lose](-)

__Given__

---

Round starts with players: [Adam, Bob](- "startRoundFor(#TEXT)")

__When__

---

- [Adam sends]
-   ```json
    {"type": "BET", "bet": 10.0, "number": 7 }
    ```
  
__And__

- [Bob sends]
-   ```json
    {"type": "BET", "bet": 10.0, "number": 2 }
    ```

__Then__

---

Server sends to players status events and game events.

- [Adam receives]
-   ```json
    {"type": "BET_PLACED", "player" : "Adam" }
    ```    
-   ```json
    {"type": "BET_PLACED", "player" : "Bob" }
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

__And__

- [Bob receives]
-   ```json
    {"type": "BET_PLACED", "player" : "Adam" }
    ```    
-   ```json
    {"type": "BET_PLACED", "player" : "Bob" }
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
    {"type": "RESULTS", "winners" : [ { "name": "Adam", "win" : 99.00} ] }
    ```

[Adam sends]: - "e:mq-send=Adam"
[Adam receives]: - "e:mq-check=Adam awaitAtMostSec=3 contains=any-order"
[Bob sends]: - "e:mq-send=Bob"
[Bob receives]: - "e:mq-check=Bob awaitAtMostSec=3 contains=any-order"