plugins {
    id("java-library")
}

dependencies {
    api(project(":protocol:api"))
    api(project(":microkernel:api"))
}
