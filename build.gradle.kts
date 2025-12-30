plugins {
    java
}

group = "ink.reactor"
version = "1.0.0"

subprojects {
    apply(plugin = "java")

    repositories {
        mavenCentral()
    }

    dependencies {
        compileOnly("org.projectlombok:lombok:1.18.42")
        annotationProcessor("org.projectlombok:lombok:1.18.42")
        compileOnly("org.jetbrains:annotations:26.0.2")

        testCompileOnly("org.projectlombok:lombok:1.18.42")
        testAnnotationProcessor("org.projectlombok:lombok:1.18.42")

        testImplementation("org.junit.jupiter:junit-jupiter:6.0.0")
        testRuntimeOnly("org.junit.platform:junit-platform-launcher")
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
}
