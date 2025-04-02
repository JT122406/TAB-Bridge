plugins {
    `java-library`
    id("io.freefair.lombok")
}

dependencies {
    compileOnly("org.jetbrains:annotations:26.0.2")
    compileOnly("net.luckperms:api:5.4")
    compileOnly("io.freefair.gradle:lombok-plugin:8.13.1")
    compileOnly("com.google.guava:guava:31.1-jre")
}