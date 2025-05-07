# Utility calculator

See [design](doc/design.md) for design details.

## Build the Project
To build the project, run:
```bash
./gradlew build
```

## Run Tests
To execute tests, use:
```bash
./gradlew test
```

## Reformat Code with Spotless
To reformat the code using Spotless, run:
```bash
./gradlew spotlessApply
```

## Configure IntelliJ IDEA
To automatically apply Spotless formatting on save in IntelliJ IDEA, follow these steps:
- install the [Spotless Applier](https://plugins.jetbrains.com/plugin/22455-spotless-applier) plugin
- open the settings (Ctrl + Alt + S)
- navigate to `Tools > Actions on Save`
- check `Run Spotles`
