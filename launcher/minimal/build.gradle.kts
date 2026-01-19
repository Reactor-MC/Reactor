plugins {
    id("com.gradleup.shadow") version "9.3.0"
    kotlin("jvm") version "2.3.0"
}

dependencies {
    implementation(project(":kernel:api"))
    implementation(project(":kernel:micro"))

    implementation(project(":sdk:bundled"))
    implementation("org.jline:jline-terminal:3.30.4")
    implementation("org.jline:jline-reader:3.30.4")

    implementation(project(":networking:api"))
    implementation(project(":networking:protocol"))
    implementation(project(":networking:internal"))
}

tasks.withType<Jar> {
    manifest {
        attributes["Main-Class"] = "ink.reactor.launcher.MinimalReactorLauncher"
    }
}
