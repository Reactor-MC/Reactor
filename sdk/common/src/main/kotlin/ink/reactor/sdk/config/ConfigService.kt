package ink.reactor.sdk.config

import java.io.IOException
import java.nio.file.Path

interface ConfigService {
    val fileExtensions: List<String>

    fun load(path: Path): ConfigSection
    fun save(path: Path, section: ConfigSection, options: SaveOptions = SaveOptions())

    @Throws(IOException::class)
    fun createIfAbsentAndLoad(fileName: String, outDestination: Path, classLoader: ClassLoader): ConfigSection

    @Throws(IOException::class)
    fun createIfAbsentAndLoad(fileName: String, classLoader: ClassLoader): ConfigSection {
        return createIfAbsentAndLoad(fileName, Path.of(fileName), classLoader)
    }
}
