package ink.reactor.sdk.config

import kotlin.reflect.KProperty

class ConfigDelegate<T : Any>(
    private val section: ConfigSection,
    private val default: T,
    private val customKey: String? = null
) {
    @Suppress("UNCHECKED_CAST")
    operator fun getValue(thisRef: Any?, property: KProperty<*>): T {
        val key = customKey ?: property.name
        return section.data[key] as? T ?: default
    }

    operator fun setValue(thisRef: Any?, property: KProperty<*>, value: T) {
        val key = customKey ?: property.name
        section.data[key] = value
    }
}
