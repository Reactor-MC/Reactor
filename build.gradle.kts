plugins {
    java
    kotlin("jvm") version "2.3.0"
}

group = "ink.reactor"
version = "1.0.0"

subprojects {
    apply(plugin = "java")
    apply(plugin = "org.jetbrains.kotlin.jvm")

    repositories {
        mavenCentral()
    }

    dependencies {
        compileOnly("org.jetbrains:annotations:26.0.2")

        testImplementation("org.junit.jupiter:junit-jupiter:6.0.0")
        testRuntimeOnly("org.junit.platform:junit-platform-launcher")

        testImplementation(kotlin("test"))
    }

    tasks.test {
        useJUnitPlatform()
    }
}

allprojects {
    apply<JavaPlugin>()

    tasks.withType<JavaCompile> {
        options.encoding = Charsets.UTF_8.name()
        options.release.set(25)
    }

    configure<JavaPluginExtension> {
        toolchain.languageVersion.set(JavaLanguageVersion.of(25))
    }

    configure<org.jetbrains.kotlin.gradle.dsl.KotlinJvmProjectExtension> {
        jvmToolchain(25)
    }
}
