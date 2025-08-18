rootProject.name = "Reactor"

include("kernel:micro")
include("kernel:api")

include("minecraft-api")

include("protocol:api")
include("protocol:netty-adapter")
include("protocol:bridge:v1.21")
include("protocol:bridge:common")

include("sdk:common")
include("sdk:bundled")

include("tools:code-generator")

include("libs:nbt")

include("launcher:minimal")
