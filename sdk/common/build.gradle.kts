plugins {
    id("java-library")
}

dependencies {
    api(project(":protocol:api"))
    api(project(":kernel:api"))
    api(project(":minecraft-api"))
}
