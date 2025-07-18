package ink.reactor.nbt.writer;

import ink.reactor.nbt.NBT;
import ink.reactor.nbt.tags.NumericalTag;
import ink.reactor.nbt.tags.Tag;
import ink.reactor.nbt.tags.array.ByteArrayTag;
import ink.reactor.nbt.tags.array.IntArrayTag;
import ink.reactor.nbt.tags.array.LongArrayTag;
import ink.reactor.nbt.tags.collection.CompoundTag;
import ink.reactor.nbt.tags.ListTag;
import lombok.experimental.UtilityClass;

import java.util.Collection;

@UtilityClass
public final class NBTJsonWriter {

    public static String toJson(final NBT nbt) {
        final StringBuilder builder = new StringBuilder();
        builder.append('{');

        int i = 0;
        for (final Tag tag : nbt.getTags()) {
            if (i != 0) {
                builder.append(',');
            }

            builder.append('"').append(tag.keyToString()).append('"').append(':');
            appendValue(tag, builder);
            i++;
        }

        builder.append('}');
        return builder.toString();
    }

    private static void appendValue(Tag value, StringBuilder builder) {
        if (value instanceof NumericalTag || value instanceof ByteArrayTag || value instanceof IntArrayTag || value instanceof LongArrayTag) {
            builder.append(value.valueToString());
            return;
        }

        if (value instanceof CompoundTag compoundTag) {
            builder.append(toJson(compoundTag.getValue()));
            return;
        }

        if (!(value instanceof ListTag listTag)) {
            builder.append('"').append(value.valueToString()).append('"');
            return;
        }

        builder.append('[');
        final Collection<? extends Tag> listTagValue = listTag.getTags();
        final int size = listTagValue.size();
        int x = 0;
        for (final Tag tag : listTagValue) {
            appendValue(tag, builder);
            if (++x != size) {
                builder.append(',');
            }
        }
        builder.append(']');
    }
}
