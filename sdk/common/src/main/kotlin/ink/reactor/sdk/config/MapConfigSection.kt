package ink.reactor.sdk.config

class MapConfigSection(
    override val data: MutableMap<String, Any?> = LinkedHashMap(),
    override val name: String = ""
) : ConfigSection
