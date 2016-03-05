package feuyeux.datastructure.map;

/**
 * 
 * @author Eric Han feuyeux@gmail.com 16/09/2012
 * @since 0.0.1
 * @version 0.0.1
 */
public class StringInt {

    private String str;
    private int value;

    public StringInt(final int value) {
        this.value = value;
    }

    // equals相等时，要求hashCode必须相等
    @Override
    public boolean equals(final Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (this.getClass() == obj.getClass()) {
            final StringInt other = (StringInt) obj;
            return value == other.value && value == other.value;
        }
        return false;
    }

    public int getValue() {
        return value;
    }

    public void setValue(final int value) {
        this.value = value;
    }

    @Override
    public int hashCode() {
        final int hashCode = 31 * value + (str == null ? 0 : str.hashCode());
        return hashCode;
    }

    @Override
    public String toString() {
        return "" + value;
    }
}
