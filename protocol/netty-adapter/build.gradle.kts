plugins {
    id("java-library")
}

dependencies {
    implementation(project(":protocol:api"))
    implementation(project(":kernel:api"))

    implementation("io.netty:netty-handler:4.2.2.Final")

    api("io.netty:netty-transport-native-epoll:4.2.2.Final:linux-x86_64")
    api("io.netty:netty-transport-native-epoll:4.2.2.Final:linux-aarch_64")
    api("io.netty:netty-transport-native-io_uring:4.2.2.Final:linux-x86_64")

    api("io.netty:netty-transport-native-kqueue:4.2.2.Final:osx-aarch_64")
}
