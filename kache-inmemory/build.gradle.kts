plugins {
    kotlin("jvm")
}

group = "io.github.virusbear.kache"
version = "1.0"

repositories {
    mavenCentral()
}

dependencies {
    implementation(kotlin("stdlib"))
    api(project(":kache-core"))
    implementation("io.github.reactivecircus.cache4k:cache4k:0.3.0")
}