# Guess Number Game

## Overview

---

Game process:

1. Players register in the game.
2. Players confirm their readiness.
3. The server starts a round of the game and gives `guess-number.countdown` * `guess-number.stepMillis` seconds to place a bet from the players on numbers from `1` to `10` with the amount of the bet.
4. After the time expires, the server generates a random number from `1` to `10`.
5. If the player guesses the number, a message is sent to him that he won with a winnings of `9.9` times the stake.
6. If the player loses then he receives a message about the loss.
7. All players receive a message with a list of winning players in which there is a nickname and the amount of winnings.

## WebSocket API

---

### Player actions

- [Player registration](PlayerRegistration.md "c:run")
- [Players readiness confirmation](PlayerReadiness.md "c:run")
- [Placing a bet](PlayerBetting.md "c:run")

### Game events

- [Round events](RoundEvents.md "c:run")

## Scenarios 

---

- [Single player](SinglePlayer.md "c:run")
- [Multiplayer](MultiPlayer.md "c:run")
