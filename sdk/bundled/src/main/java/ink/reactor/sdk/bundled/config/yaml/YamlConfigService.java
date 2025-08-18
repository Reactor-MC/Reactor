package ink.reactor.sdk.bundled.config.yaml;

import ink.reactor.sdk.bundled.config.AbstractConfigService;
import ink.reactor.sdk.config.ConfigSection;
import ink.reactor.sdk.config.GenericConfigSection;
import ink.reactor.sdk.config.SaveOptions;
import ink.reactor.sdk.config.exception.ConfigLoadException;
import ink.reactor.sdk.config.exception.ConfigSaveException;
import org.snakeyaml.engine.v2.api.Load;
import org.snakeyaml.engine.v2.api.LoadSettings;

import java.io.BufferedReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.Collection;
import java.util.List;
import java.util.Map;

public final class YamlConfigService extends AbstractConfigService {

    private Load loader;

    @Override
    @SuppressWarnings("unchecked")
    public ConfigSection load(final Path path) {
        try(final BufferedReader bufferedReader = Files.newBufferedReader(path)) {
            final Object object = getLoader().loadFromReader(bufferedReader);
            if (!(object instanceof Map<?,?>)) {
                return new GenericConfigSection(Map.of("", object));
            }
            return new GenericConfigSection((Map<String, Object>)object);
        } catch (IOException e) {
            throw new ConfigLoadException("Error on load yaml file. Path: " + path, e);
        }
    }

    @Override
    public void save(final Path path, final ConfigSection section, final SaveOptions options) {
        try {
            Files.writeString(path, new SectionToYamlConverter(options.indentSpaces()).toYaml(section), StandardOpenOption.WRITE);
        } catch (IOException e) {
            throw new ConfigSaveException("Error on save yaml file. Path: " + path + ". Options: " + options, e);
        }
    }

    @Override
    public Collection<String> fileExtensions() {
        return List.of("yaml", "yml");
    }

    private Load getLoader() {
        if (loader != null) {
            return loader;
        }
        final LoadSettings settings = LoadSettings.builder()
            .setParseComments(false)
            .build();
        loader = new Load(settings, new YamlCustomConstructor(settings));
        return loader;
    }
}
