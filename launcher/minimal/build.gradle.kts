plugins {
    id("com.github.johnrengelman.shadow") version "8.1.1"
}

dependencies {
    implementation(project(":protocol:api"))
    implementation(project(":protocol:netty-adapter"))
    implementation(project(":protocol:bridge:common"))
    implementation(project(":protocol:bridge:v1.21"))

    implementation(project(":microkernel:api"))
    implementation(project(":microkernel:impl"))

    implementation(project(":sdk:bundled"))
    implementation("org.jline:jline:3.30.4")
}

tasks.withType<Jar> {
    manifest {
        attributes["Main-Class"] = "ink.reactor.launcher.ReactorLauncher"
    }
}
