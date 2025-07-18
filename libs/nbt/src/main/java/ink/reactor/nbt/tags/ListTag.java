package ink.reactor.nbt.tags;

import java.util.Collection;

public abstract class ListTag extends Tag {

    public ListTag(final Object key) {
        super(key);
    }

    public abstract Collection<? extends Tag> getTags();
    public abstract byte listId();
}
