# Round events

## Overview

After a start of the game round server sends to players `COUNTDOWN` and `ROUND_RESULT` events.

After an end of the round server sends to players `PLAYER_RESULT` and `RESULTS` events.

## Examples

### Before each

#### [ ](- "before")

[There is no logged in players.](- "logoutAll()")

### Round events

After player starts a round he receives `COUNTDOWN`, `ROUND_RESULT`, `PLAYER_RESULT` and `RESULTS` events from the server.

#### [Round events](-)

__When__

---

Player [Adam](- "startRoundFor(#TEXT)") starts a round.  

__Then__

---

- [Player receive events][receive]
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

[send]: - "e:mq-send=Adam"
[receive]: - "e:mq-check=Adam awaitAtMostSec=3"