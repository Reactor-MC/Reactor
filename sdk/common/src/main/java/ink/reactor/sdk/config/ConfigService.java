package ink.reactor.sdk.config;

import java.io.IOException;
import java.nio.file.Path;
import java.util.Collection;

public interface ConfigService {
    ConfigSection load(final Path path);
    void save(final Path path, final ConfigSection section, final SaveOptions options);

    ConfigSection createIfAbsentAndLoad(final String fileName, final Path outDestination, final ClassLoader classLoader) throws IOException;
    default ConfigSection createIfAbsentAndLoad(final String fileName, final ClassLoader classLoader) throws IOException {
        return createIfAbsentAndLoad(fileName, Path.of(fileName), classLoader);
    }

    Collection<String> fileExtensions();
}
