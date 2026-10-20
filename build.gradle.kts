import org.apache.commons.lang3.SystemUtils

plugins {
    idea
    java
    id("gg.essential.loom") version "1.6.+"
    id("dev.architectury.architectury-pack200") version "0.1.3"
}

// Project Properties
val baseGroup: String by project
val mcVersion: String by project
val version: String by project
val modid: String by project

// Toolchain: Java 8 bytecode
java {
    toolchain.languageVersion.set(JavaLanguageVersion.of(8))
}

// Loom Minecraft & Forge Configuration
loom {
    runs {
        named("client") {
            programArgs("--tweakClass", "optifine.OptiFineForgeTweaker")
            if (SystemUtils.IS_OS_MAC_OSX) {
                vmArgs.remove("-XstartOnFirstThread")
            }
        }
        remove(getByName("server"))
    }
    forge {
        pack200Provider.set(dev.architectury.pack200.java.Pack200Adapter())
    }
}

// Repositories
repositories {
    mavenCentral()
    maven("https://pkgs.dev.azure.com/djtheredstoner/DevAuth/_packaging/public/maven/v1")
    maven("https://jitpack.io")
}

// Dependencies
dependencies {
    minecraft("com.mojang:minecraft:1.8.9")
    mappings("de.oceanlabs.mcp:mcp_stable:22-1.8.9")
    forge("net.minecraftforge:forge:1.8.9-11.15.1.2318-1.8.9")

    // OptiFine 1.8.9 HD U M5 (Remapped to MCP named)
    implementation(files("libs/OptiFine_1.8.9_HD_U_M5_MAPPED.jar"))

    // Dev authentication for quick launching
    runtimeOnly("me.djtheredstoner:DevAuth-forge-legacy:1.2.1")
}

// Tasks
tasks.withType<JavaCompile> {
    options.encoding = "UTF-8"
}

tasks.withType<Jar> {
    archiveBaseName.set(modid)
    manifest.attributes.run {
        this["FMLCorePluginContainsFMLMod"] = "true"
        this["ForceLoadAsMod"] = "true"
        this["ModSide"] = "CLIENT"
    }
}

tasks.processResources {
    inputs.property("version", project.version)
    inputs.property("mcversion", mcVersion)
    inputs.property("modid", modid)

    filesMatching("mcmod.info") {
        expand(inputs.properties)
    }
}

val javaToolchains = project.extensions.getByType<JavaToolchainService>()

tasks.named<JavaExec>("runClient") {
    javaLauncher.set(javaToolchains.launcherFor {
        languageVersion.set(JavaLanguageVersion.of(8))
    })
}
