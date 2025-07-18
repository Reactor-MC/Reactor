rootProject.name = "Reactor"

include("microkernel:api")
include("microkernel:impl")

include("protocol:api")
include("protocol:netty-adapter")
include("protocol:bridge:v1.21")
include("protocol:bridge:common")

include("launcher:minimal")

include("sdk:common")
include("sdk:bundled")

include("tools:code-generator")

include("libs:nbt")
