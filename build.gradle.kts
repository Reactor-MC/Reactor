plugins {
    java
    id("io.freefair.lombok") version "8.14"
}

group = "ink.reactor"
version = "1.0.0"

subprojects {
    apply(plugin = "java")
    apply(plugin = "io.freefair.lombok")

    repositories {
        mavenCentral()
    }

    dependencies {
        testImplementation(platform("org.junit:junit-bom:5.10.0"))
        testImplementation("org.junit.jupiter:junit-jupiter")
    }

    tasks.test {
        useJUnitPlatform()
    }
}

allprojects {
    apply<JavaPlugin>()

    tasks.withType<JavaCompile> {
        options.encoding = Charsets.UTF_8.name()
        options.release.set(24)
    }

    configure<JavaPluginExtension> {
        toolchain.languageVersion.set(JavaLanguageVersion.of(24))
    }
}
