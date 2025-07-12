rootProject.name = "Reactor"

include("microkernel:api")
include("microkernel:impl")

include("protocol:api")
include("protocol:netty-adapter")

include("launcher:minimal")

include("sdk:common")
include("sdk:bundled")
