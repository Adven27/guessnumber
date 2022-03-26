# Guess Number over Web Socket 

## Running

1. Run app:

```shell
./gradlew bootRun
```

2. Access a dummy UI at [http://localhost:8080/](http://localhost:8080/)


## Tests

### Run all tests:

```shell
./gradlew check
```

### Unit tests 

[src/test/java/com/example/game/guessnumber/GameTest.java](src/test/java/com/example/game/guessnumber/GameTest.java)

### Living specification

Living specification (integration tests + documentation):

- [src/test/resources/specs/Specs.md](src/test/resources/specs/Specs.md)
- [src/test/java/specs/Specs.java](src/test/java/specs/Specs.java)

Run:

```shell
./gradlew test --tests "specs.Specs"
```

See report in browser:  [/build/reports/specs/specs/Specs.html](/build/reports/specs/specs/Specs.html) 



