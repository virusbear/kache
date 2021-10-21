plugins {
    kotlin("jvm") version "1.5.30" apply false
}

group = "io.github.virusbear.kache"
version = "1.0"

allprojects {
    repositories {
        mavenCentral()
        mavenLocal()
        google()
    }

    tasks.withType<org.jetbrains.kotlin.gradle.tasks.KotlinCompile> {
        sourceCompatibility = "11"
        targetCompatibility = "11"
    }
}