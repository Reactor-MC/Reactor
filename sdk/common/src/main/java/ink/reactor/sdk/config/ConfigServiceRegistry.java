package ink.reactor.sdk.config;

import ink.reactor.sdk.config.exception.UnsupportedConfigFormatException;
import lombok.experimental.UtilityClass;

import java.util.HashMap;
import java.util.Map;

@UtilityClass
public final class ConfigServiceRegistry {
    private static final Map<String, ConfigService> PROVIDERS = new HashMap<>();

    public static ConfigService getProvider(final String configFormat) {
        final ConfigService provider = PROVIDERS.get(configFormat);
        if (provider == null) {
            throw new UnsupportedConfigFormatException(configFormat);
        }
        return provider;
    }

    public static void addProvider(final ConfigService provider) {
        for (final String fileExtension : provider.fileExtensions()) {
            PROVIDERS.put(fileExtension, provider);
        }
    }
}
