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
}
