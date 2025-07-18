package ink.reactor.sdk.bundled.test.json;

import ink.reactor.sdk.bundled.config.json.converter.MinifierJsonFormatConverter;
import ink.reactor.sdk.bundled.config.json.decoder.JsonDecoder;
import ink.reactor.sdk.config.ConfigSection;
import ink.reactor.sdk.config.GenericConfigSection;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public final class JsonTest {

    @Test
    public void testMinifier() throws IOException {
        final ConfigSection exampleSection = createExampleSection();
        assertEquals(new String(loadJsonFile("example-minify.json")), new MinifierJsonFormatConverter().toJson(exampleSection));
    }

    @Test
    public void testDecoder() throws IOException {
        final JsonDecoder jsonDecoder = new JsonDecoder(loadJsonFile("example.json"));

        final ConfigSection section = jsonDecoder.decode();
        final ConfigSection exampleSection = createExampleSection();

        assertEquals(section, exampleSection);
    }

    private ConfigSection createExampleSection() {
        final ConfigSection section = new GenericConfigSection();
        section.set("string", "Hello World!");

        final ConfigSection numbers = new GenericConfigSection();
        numbers.set("int", 1000);
        numbers.set("exponential", 1e3);
        numbers.set("decimal", 3.14159);

        final GenericConfigSection sectionArrays = new GenericConfigSection();
        sectionArrays.set("subsection1", "Hi");
        final List<ConfigSection> listSections = new ArrayList<>();
        listSections.add(sectionArrays);

        final List<Number> list = new ArrayList<>();
        list.add(343597383674L);
        list.add(-1);
        list.add(2.214);
        list.add(1e1);

        section.set("numbers", numbers);
        section.set("section-arrays", listSections);
        section.set("number-arrays", list);

        return section;
    }

    private byte[] loadJsonFile(final String file) throws IOException {
        try(InputStream inputStream = JsonTest.class.getClassLoader().getResourceAsStream(file)) {
            assertNotNull(inputStream, "Can't found the file " + file);
            return inputStream.readAllBytes();
        }
    }
}
