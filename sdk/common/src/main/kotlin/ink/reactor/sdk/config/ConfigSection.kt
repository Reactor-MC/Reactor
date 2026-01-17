package ink.reactor.sdk.config

interface ConfigSection {
    val name: String
    val data: MutableMap<String, Any?>

    operator fun get(key: String): Any? = data[key]
    operator fun set(key: String, value: Any?) { data[key] = value }

    infix fun String.to(value: Any?) {
        data[this] = value
    }

    fun getString(key: String): String? = get(key) as? String
    fun getInt(key: String): Int = (get(key) as? Number)?.toInt() ?: 0
    fun getLong(key: String): Long = (get(key) as? Number)?.toLong() ?: 0L
    fun getDouble(key: String): Double = (get(key) as? Number)?.toDouble() ?: 0.0
    fun getBoolean(key: String): Boolean = get(key) as? Boolean ?: false

    @Suppress("UNCHECKED_CAST")
    fun <T> getOrDefault(key: String, default: T): T {
        val value = data[key] ?: return default
        return try {
            value as T
        } catch (_: ClassCastException) {
            default
        }
    }

    @Suppress("UNCHECKED_CAST")
    fun getSection(key: String): ConfigSection? {
        val sectionData = data[key] as? MutableMap<String, Any?> ?: return null
        return MapConfigSection(sectionData, key)
    }

    fun getOrCreateSection(key: String): ConfigSection {
        val existing = getSection(key);
        if (existing != null) {
            return existing
        }
        val newMap = LinkedHashMap<String, Any?>()
        data[key] = newMap
        return MapConfigSection(newMap, key)
    }

    fun <T : Any> delegate(default: T, key: String? = null) = ConfigDelegate(this, default, key)
}
