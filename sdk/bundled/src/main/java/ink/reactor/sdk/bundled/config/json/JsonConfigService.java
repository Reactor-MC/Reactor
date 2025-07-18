package ink.reactor.sdk.bundled.config.json;

import ink.reactor.sdk.bundled.config.AbstractConfigService;
import ink.reactor.sdk.bundled.config.json.converter.MinifierJsonFormatConverter;
import ink.reactor.sdk.bundled.config.json.converter.PrettyJsonFormatConverter;
import ink.reactor.sdk.bundled.config.json.decoder.JsonDecoder;
import ink.reactor.sdk.bundled.config.json.decoder.exception.JsonDecoderException;
import ink.reactor.sdk.config.ConfigSection;
import ink.reactor.sdk.config.GenericConfigSection;
import ink.reactor.sdk.config.SaveOptions;
import ink.reactor.sdk.config.exception.ConfigLoadException;
import ink.reactor.sdk.config.exception.ConfigSaveException;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.Collection;
import java.util.List;

public final class JsonConfigService extends AbstractConfigService {

    @Override
    public ConfigSection load(final Path path) {
        final File file = path.toFile();
        final long length = file.length();
        if (length > Integer.MAX_VALUE) {
            throw new ConfigLoadException("Json file weights more than " + Integer.MAX_VALUE + " bytes");
        }
        final byte[] buffer = new byte[(int)length];
        final JsonDecoder jsonDecoder = new JsonDecoder(buffer);
        try(final FileInputStream inputStream = new FileInputStream(file)) {
            if (inputStream.read(buffer) == -1) {
                return new GenericConfigSection();
            }
            return jsonDecoder.decode();
        } catch (IOException e) {
            throw new ConfigLoadException("Error on read to json file. Path: " + path, e);
        } catch (JsonDecoderException e) {
            throw new ConfigLoadException("Error on decode json file. Path: " + path + ". Index: " + jsonDecoder.getIndex() + ". Buffer Length: " + jsonDecoder.getBuffer().length, e);
        }
    }

    @Override
    public void save(final Path path, final ConfigSection section, final SaveOptions options) {
        try {
            final String json = options.prettyFormat()
                ? new PrettyJsonFormatConverter(options.indentSpaces()).toJson(section)
                : new MinifierJsonFormatConverter().toJson(section);

            Files.writeString(path, json, StandardOpenOption.WRITE);
        } catch (IOException e) {
            throw new ConfigSaveException("Error on save json file. Path: " + path + ". Options: " + options, e);
        }
    }

    @Override
    public Collection<String> fileExtensions() {
        return List.of("json");
    }
}
