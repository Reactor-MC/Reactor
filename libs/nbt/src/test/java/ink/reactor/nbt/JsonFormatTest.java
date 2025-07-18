package ink.reactor.nbt;

import ink.reactor.nbt.adapter.NBTMap;
import ink.reactor.nbt.writer.NBTJsonWriter;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.LinkedHashMap;

public class JsonFormatTest {

    @Test
    public void testJsonWriter() {
        final NBT nbt = new NBTMap(new LinkedHashMap<>());
        nbt.addByte("byte", (byte)2);
        nbt.addBoolean("boolean", true);
        nbt.addIntArray("intArray", 2, 3, 5);

        final NBT subSection = new NBTMap(new LinkedHashMap<>());
        subSection.addFloat("float", 3.14F);

        nbt.addSection("section", subSection);

        Assertions.assertEquals("{\"byte\":2,\"boolean\":1,\"intArray\":[2, 3, 5],\"section\":{\"float\":3.14}}", NBTJsonWriter.toJson(nbt));
    }
}
