package ink.reactor.nbt.tags;

import ink.reactor.nbt.buffer.NBTWriteBuffer;
import lombok.Getter;

@Getter
public abstract class Tag {

    private final Object key;

    public Tag(Object key) {
        this.key = key;
    }

    public abstract TagId getId();
    public abstract Object getValue();
    public abstract int sizeInBytes();

    public abstract void write(final NBTWriteBuffer buffer);

    @Override
    public String toString() {
        return getId().name() + '{' + keyToString() + " = " + valueToString() + '}';
    }

    public String valueToString() {
        return getValue().toString();
    }

    public String keyToString() {
        return key instanceof byte[] bytes ? new String(bytes) : key.toString();
    }
}
