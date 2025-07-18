package ink.reactor.nbt.tags;

public abstract class NumericalTag extends Tag {

    public NumericalTag(final Object key) {
        super(key);
    }

    public abstract byte byteValue();
    public abstract short shortValue();
    public abstract float floatValue();
    public abstract double doubleValue();
    public abstract int intValue();
    public abstract long longValue();
}
