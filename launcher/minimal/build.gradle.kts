plugins {
    id("com.github.johnrengelman.shadow") version "8.1.1"
}

dependencies {
    implementation(project(":kernel:api"))
    implementation(project(":kernel:micro"))

    implementation(kotlin("test"))

    implementation(project(":sdk:bundled"))
    implementation("org.jline:jline-terminal:3.30.4")
    implementation("org.jline:jline-reader:3.30.4")
}

tasks.withType<Jar> {
    manifest {
        attributes["Main-Class"] = "ink.reactor.launcher.ReactorLauncher"
    }
}
