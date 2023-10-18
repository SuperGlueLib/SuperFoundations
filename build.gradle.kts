plugins {
    kotlin("jvm") version "1.9.0"
    `maven-publish`
}

val plVer = "1.2.1"
group = "com.github.supergluelib"
version = plVer

repositories {
    mavenCentral()
    maven("https://hub.spigotmc.org/nexus/content/repositories/snapshots/")
}

dependencies {
    compileOnly("org.spigotmc:spigot-api:1.19.4-R0.1-SNAPSHOT")
}

publishing.publications.create<MavenPublication>("maven") {
    groupId = "com.github.supergluelib"
    artifactId = "superfoundations"
    version = plVer

    from(components["java"])
}
