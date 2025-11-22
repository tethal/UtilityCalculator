# Utility Calculator

TODO – brief introduction of the project itself.

## 📂 Project Structure

The project is organized as a multi-module Gradle build:

* **`core`**: Contains shared business logic, domain models, and services used by other modules.
* **`web`**: Web application built with Spring.
* **`cli`**: Command-line interface.

## 🛠 Technologies

* **Language**: Java
* **Build Tool**: Gradle

### Prerequisites

Before running the project, make sure you have:

* **Java 21** (ensure the `JAVA_HOME` environment variable points to this JDK)
* **Git**

## 🔨 Building the Project

The project uses the Gradle wrapper, so you don’t need to install Gradle globally:

```bash
./gradlew clean build
```

## ▶️ Running the Application

### Web Application
Start the web application by running the following command:
```bash
./gradlew bootRun
```
The application will be available at:
`http://localhost:8080` (unless configured otherwise)

### CLI Application
Run the CLI tool by passing an input file as a parameter:
```bash
./gradlew run <file>
```

## 🧪 Testing
Run tests in all modules:
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
