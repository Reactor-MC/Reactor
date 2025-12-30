package ink.reactor.sdk.bundled.config;

import ink.reactor.sdk.config.ConfigService;
import ink.reactor.sdk.config.section.ConfigSection;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;

public abstract class AbstractConfigService implements ConfigService {

    @Override
    public ConfigSection createIfAbsentAndLoad(final String fileName, final Path outDestination, final ClassLoader classLoader) throws IOException {
        if (Files.exists(outDestination)) {
            return load(outDestination);
        }

        try(final InputStream inputStream = classLoader.getResourceAsStream(fileName)) {
            if (inputStream == null) {
                throw new FileNotFoundException("Can't found the file " + fileName + " inside of the jar " + classLoader);
            }
            Files.copy(inputStream, outDestination, StandardCopyOption.REPLACE_EXISTING);
            return load(outDestination);
        }
    }
}
