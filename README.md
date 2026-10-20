# Zygot Client

Zygot Client is a clean, lightweight Minecraft 1.8.9 client built on Minecraft Forge with integrated OptiFine for optimal frame rates and visual fidelity.

## Requirements

* Java Development Kit (JDK) 8 or 21 (build system targets Java 8 bytecode)
* Minecraft 1.8.9

## Building from Source

To compile the client jar:

```bash
./gradlew build
```

The compiled mod jar will be located in `build/libs/`.

## Running the Client

### Via Command Line
```bash
./gradlew runClient
```

### Via IntelliJ IDEA
1. Open the project directory in IntelliJ IDEA.
2. Run the Gradle task:
   ```bash
   ./gradlew genIntellijRuns
   ```
3. Select `Minecraft Client` from the Run Configurations dropdown and launch.

## Features

* Standard Minecraft Forge 1.8.9 environment
* OptiFine 1.8.9 HD U M5 integration
* Fast startup and low memory footprint

## License

This project is licensed under the GNU General Public License v3.0. See the [LICENSE](LICENSE) file for details.
