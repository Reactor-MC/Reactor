package ink.reactor.sdk.bundled.config

import ink.reactor.sdk.config.ConfigSection
import ink.reactor.sdk.config.ConfigService
import java.io.FileNotFoundException
import java.io.IOException
import java.io.InputStream
import java.nio.file.Files
import java.nio.file.Path
import java.nio.file.StandardCopyOption
import kotlin.io.path.createDirectories
import kotlin.io.path.exists

abstract class AbstractConfigService : ConfigService {

    @Throws(IOException::class)
    override fun createIfAbsentAndLoad(
        fileName: String,
        outDestination: Path,
        classLoader: ClassLoader
    ): ConfigSection {
        if (outDestination.exists()) {
            return load(outDestination)
        }

        val inputStream: InputStream = classLoader.getResourceAsStream(fileName)
            ?: throw FileNotFoundException("Can't find the file '$fileName' inside of the jar.")

        inputStream.use { input ->
            outDestination.parent?.createDirectories()
            Files.copy(input, outDestination, StandardCopyOption.REPLACE_EXISTING)
        }

        return load(outDestination)
    }
}
