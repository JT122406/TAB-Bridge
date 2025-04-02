plugins {
    id("fabric-loom")
}

repositories {
    maven("https://maven.nucleoid.xyz/")
}

dependencies {
    minecraft("com.mojang:minecraft:1.21.5")
    mappings(loom.officialMojangMappings())
    modImplementation("net.fabricmc:fabric-loader:0.16.12")
    modApi("net.fabricmc.fabric-api:fabric-api:0.119.6+1.21.5")

    implementation(projects.shared)
    modImplementation("eu.pb4:placeholder-api:2.5.0+1.21.2")
}

tasks {
    compileJava {
        options.release.set(21)
    }
}