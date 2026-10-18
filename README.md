# Zygot Client

Zygot Client is a modern, high-performance Minecraft 1.8.9 PvP client built on Forge. Designed with modern user interface standards reminiscent of Feather Client and Lunar Client, it integrates glassmorphic UI components, smooth animations, an extensible cosmetics engine, and over 35 PvP, HUD, and quality-of-life modules.

## Key Features

- **Modern Interface**: Translucent glass panels, rounded corners, drop shadows, and Comfortaa TrueType typography with pre-baked anti-aliased glyph pages.
- **Dynamic Theming**: Six built-in color schemes (Zygot Violet, Midnight Cyber, Crimson Blood, Emerald PvP, Sunset Amber, Clean Light) and full custom RGB hexadecimal palette customization.
- **Customizable Backgrounds**: Selectable main menu presentation modes including interactive particle meshes, flowing gradients, and static wallpapers.
- **Integrated 3D Cosmetics**: Modular cosmetics system supporting custom capes with realistic wave physics, animated dragon and crystal wings, halos, and hats, accompanied by an interactive 3D wardrobe screen.
- **Extensive Module Suite**: Over 35 built-in modules:
  - PvP: 1.7 animations (blockhit, bow, rod, swing), keystrokes, CPS counter, reach display, combo tracker, hit color, custom crosshair.
  - HUD: FPS, ping, coordinates, direction, scoreboard, bossbar, clock, speedometer, day counter.
  - Render: Fullbright, smooth zoom, time changer, motion blur, menu blur, item physics, block overlay, low fire.
  - Performance: Particle culling, particle multiplier, fast math optimizations.
  - Quality of Life: Toggle sprint, toggle sneak, auto GG, chat tweaks, nick hider, TNT timer.
- **In-Game HUD Editor**: Grid-based HUD customization with magnetic snapping, edge alignment, live scaling, and coordinate resets.
- **Centralized White-Label Architecture**: Unified branding profile system allowing client name, links, assets, and metadata to be customized from a single configuration file.

## Technical Architecture

- **Minecraft Version**: 1.8.9 (Forge 11.15.1.2318)
- **Build System**: Gradle 8.8
- **Toolchain**: Architectury / Essential Loom (Java 8 toolchain targeting Java 8 bytecode, with Gradle daemon execution on Java 17 or 21)
- **Bytecode Manipulation**: SpongePowered Mixin 0.7.11 with annotation processor 0.8.5
- **Development Authentication**: DevAuth integration for testing directly within development environments

## Prerequisites

- Java Development Kit (JDK) 17 or 21 (required to execute the modern Gradle 8 build daemon)
- Java Development Kit (JDK) 8 (used by the Gradle toolchain to compile target bytecode)

## Building from Source

Clone the repository and build the project using the included Gradle wrapper:

```bash
git clone https://github.com/senu-io/ZygotClient.git
cd ZygotClient
./gradlew build
```

The compiled mod JAR will be located in the `build/libs/` directory.

## Running in Development

To launch the Minecraft client directly in a development environment:

```bash
./gradlew runClient
```

### IntelliJ IDEA

1. Open IntelliJ IDEA and select `File` -> `Open...`, then select the repository root folder.
2. Ensure the Gradle JVM in `Settings` -> `Build, Execution, Deployment` -> `Build Tools` -> `Gradle` is set to JDK 17, JDK 21, or the embedded JetBrains Runtime.
3. Select the `Minecraft Client` run configuration from the top-right toolbar and click Run.

## License

This project is licensed under the terms specified in the LICENSE file.
