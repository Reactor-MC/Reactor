plugins {
    id("java-library")
}
dependencies {
    api(project(":sdk:common"))
    implementation("org.snakeyaml:snakeyaml-engine:3.0.1")
}
