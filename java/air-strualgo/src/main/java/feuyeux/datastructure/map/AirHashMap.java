package feuyeux.datastructure.map;

/**
 * 
 * @author
 * Eric Han feuyeux@gmail.com
 * 16/09/2012
 * @since  0.0.1
 * @version 0.0.1
 */
import java.io.IOException;
import java.io.Serializable;
import java.util.AbstractMap;
import java.util.AbstractSet;
import java.util.ConcurrentModificationException;
import java.util.Iterator;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Set;

public class AirHashMap<K, V> extends AbstractMap<K, V> implements Map<K, V>, Cloneable, Serializable {
    private static final long serialVersionUID = 3476735979928755996L;
    static final int DEFAULT_INITIAL_CAPACITY = 16;// 初始容量
    static final int MAXIMUM_CAPACITY = 1 << 30;
    static final float DEFAULT_LOAD_FACTOR = 0.75f;// 负载因子
    transient AirEntry<K, V>[] table;// hash数组
    transient int size;
    int threshold;// 阈值
    final float loadFactor;
    transient int modCount;// 修改次数

    public AirHashMap(int initialCapacity, final float loadFactor) {
        if (initialCapacity < 0) {
            throw new IllegalArgumentException("Illegal initial capacity: " + initialCapacity);
        }
        if (initialCapacity > AirHashMap.MAXIMUM_CAPACITY) {
            initialCapacity = AirHashMap.MAXIMUM_CAPACITY;
        }
        if (loadFactor <= 0 || Float.isNaN(loadFactor)) {
            throw new IllegalArgumentException("Illegal load factor: " + loadFactor);
        }

        // Find a power of 2 >= initialCapacity
        int capacity = 1;
        while (capacity < initialCapacity) {
            capacity <<= 1;
        }

        this.loadFactor = loadFactor;
        threshold = (int) (capacity * loadFactor);
        table = new AirEntry[capacity];
        init();
    }

    public AirHashMap(final int initialCapacity) {
        this(initialCapacity, AirHashMap.DEFAULT_LOAD_FACTOR);
    }

    public AirHashMap() {
        this.loadFactor = AirHashMap.DEFAULT_LOAD_FACTOR;
        threshold = (int) (AirHashMap.DEFAULT_INITIAL_CAPACITY * AirHashMap.DEFAULT_LOAD_FACTOR);
        table = new AirEntry[AirHashMap.DEFAULT_INITIAL_CAPACITY];
        init();
    }

    public AirHashMap(final Map<? extends K, ? extends V> m) {
        this(Math.max((int) (m.size() / AirHashMap.DEFAULT_LOAD_FACTOR) + 1, AirHashMap.DEFAULT_INITIAL_CAPACITY), AirHashMap.DEFAULT_LOAD_FACTOR);
        putAllForCreate(m);
    }

    void init() {
    }

    static int hash(int h) {
        // This function ensures that hashCodes that differ only by
        // constant multiples at each bit position have a bounded
        // number of collisions (approximately 8 at default load factor).
        h ^= h >>> 20 ^ h >>> 12;
        return h ^ h >>> 7 ^ h >>> 4;
    }

    static int indexFor(final int h, final int length) {
        return h & length - 1;
    }

    @Override
    public int size() {
        return size;
    }

    @Override
    public boolean isEmpty() {
        return size == 0;
    }

    @Override
    public V get(final Object key) {
        if (key == null) {
            return getForNullKey();
        }
        final int hash = hash(key.hashCode());
        for (AirEntry<K, V> e = table[indexFor(hash, table.length)]; e != null; e = e.next) {
            Object k;
            if (e.hash == hash && ((k = e.key) == key || key.equals(k))) {
                return e.value;
            }
        }
        return null;
    }

    private V getForNullKey() {
        for (AirEntry<K, V> e = table[0]; e != null; e = e.next) {
            if (e.key == null) {
                return e.value;
            }
        }
        return null;
    }

    @Override
    public boolean containsKey(final Object key) {
        return getEntry(key) != null;
    }

    final AirEntry<K, V> getEntry(final Object key) {
        final int hash = key == null ? 0 : hash(key.hashCode());
        for (AirEntry<K, V> e = table[indexFor(hash, table.length)]; e != null; e = e.next) {
            Object k;
            if (e.hash == hash && ((k = e.key) == key || key != null && key.equals(k))) {
                return e;
            }
        }
        return null;
    }

    @Override
    public V put(final K key, final V value) {
        if (key == null) {
            return putForNullKey(value);
        }
        final int hash = hash(key.hashCode());
        final int i = indexFor(hash, table.length);
        for (AirEntry<K, V> e = table[i]; e != null; e = e.next) {
            Object k;
            if (e.hash == hash && ((k = e.key) == key || key.equals(k))) {
                final V oldValue = e.value;
                e.value = value;
                e.recordAccess(this);
                return oldValue;
            }
        }

        modCount++;
        addEntry(hash, key, value, i);
        return null;
    }

    private V putForNullKey(final V value) {
        for (AirEntry<K, V> e = table[0]; e != null; e = e.next) {
            if (e.key == null) {
                final V oldValue = e.value;
                e.value = value;
                e.recordAccess(this);
                return oldValue;
            }
        }
        modCount++;
        addEntry(0, null, value, 0);
        return null;
    }

    private void putForCreate(final K key, final V value) {
        final int hash = key == null ? 0 : hash(key.hashCode());
        final int i = indexFor(hash, table.length);

        /**
         * Look for preexisting entry for key. This will never happen for clone or deserialize. It will only happen for construction if the input Map is a
         * sorted map whose ordering is inconsistent w/ equals.
         */
        for (AirEntry<K, V> e = table[i]; e != null; e = e.next) {
            Object k;
            if (e.hash == hash && ((k = e.key) == key || key != null && key.equals(k))) {
                e.value = value;
                return;
            }
        }

        createEntry(hash, key, value, i);
    }

    private void putAllForCreate(final Map<? extends K, ? extends V> m) {
        for (final Map.Entry<? extends K, ? extends V> e : m.entrySet()) {
            putForCreate(e.getKey(), e.getValue());
        }
    }

    void resize(final int newCapacity) {
        final AirEntry[] oldTable = table;
        final int oldCapacity = oldTable.length;
        if (oldCapacity == AirHashMap.MAXIMUM_CAPACITY) {
            threshold = Integer.MAX_VALUE;
            return;
        }

        final AirEntry[] newTable = new AirEntry[newCapacity];
        transfer(newTable);
        table = newTable;
        threshold = (int) (newCapacity * loadFactor);
    }

    void transfer(final AirEntry[] newTable) {
        final AirEntry[] src = table;
        final int newCapacity = newTable.length;
        for (int j = 0; j < src.length; j++) {
            AirEntry<K, V> e = src[j];
            if (e != null) {
                src[j] = null;
                do {
                    final AirEntry<K, V> next = e.next;
                    final int i = indexFor(e.hash, newCapacity);
                    e.next = newTable[i];
                    newTable[i] = e;
                    e = next;
                } while (e != null);
            }
        }
    }

    @Override
    public void putAll(final Map<? extends K, ? extends V> m) {
        final int numKeysToBeAdded = m.size();
        if (numKeysToBeAdded == 0) {
            return;
        }

        if (numKeysToBeAdded > threshold) {
            int targetCapacity = (int) (numKeysToBeAdded / loadFactor + 1);
            if (targetCapacity > AirHashMap.MAXIMUM_CAPACITY) {
                targetCapacity = AirHashMap.MAXIMUM_CAPACITY;
            }
            int newCapacity = table.length;
            while (newCapacity < targetCapacity) {
                newCapacity <<= 1;
            }
            if (newCapacity > table.length) {
                resize(newCapacity);
            }
        }

        for (final Map.Entry<? extends K, ? extends V> e : m.entrySet()) {
            put(e.getKey(), e.getValue());
        }
    }

    @Override
    public V remove(final Object key) {
        final AirEntry<K, V> e = removeEntryForKey(key);
        return e == null ? null : e.value;
    }

    final AirEntry<K, V> removeEntryForKey(final Object key) {
        final int hash = key == null ? 0 : hash(key.hashCode());
        final int i = indexFor(hash, table.length);
        AirEntry<K, V> prev = table[i];
        AirEntry<K, V> e = prev;

        while (e != null) {
            final AirEntry<K, V> next = e.next;
            Object k;
            if (e.hash == hash && ((k = e.key) == key || key != null && key.equals(k))) {
                modCount++;
                size--;
                if (prev == e) {
                    table[i] = next;
                } else {
                    prev.next = next;
                }
                e.recordRemoval(this);
                return e;
            }
            prev = e;
            e = next;
        }

        return e;
    }

    final AirEntry<K, V> removeMapping(final Object o) {
        if (!(o instanceof Map.Entry)) {
            return null;
        }

        final Map.Entry<K, V> entry = (Map.Entry<K, V>) o;
        final Object key = entry.getKey();
        final int hash = key == null ? 0 : hash(key.hashCode());
        final int i = indexFor(hash, table.length);
        AirEntry<K, V> prev = table[i];
        AirEntry<K, V> e = prev;

        while (e != null) {
            final AirEntry<K, V> next = e.next;
            if (e.hash == hash && e.equals(entry)) {
                modCount++;
                size--;
                if (prev == e) {
                    table[i] = next;
                } else {
                    prev.next = next;
                }
                e.recordRemoval(this);
                return e;
            }
            prev = e;
            e = next;
        }

        return e;
    }

    @Override
    public void clear() {
        modCount++;
        final AirEntry[] tab = table;
        for (int i = 0; i < tab.length; i++) {
            tab[i] = null;
        }
        size = 0;
    }

    @Override
    public boolean containsValue(final Object value) {
        if (value == null) {
            return containsNullValue();
        }

        final AirEntry[] tab = table;
        for (int i = 0; i < tab.length; i++) {
            for (AirEntry e = tab[i]; e != null; e = e.next) {
                if (value.equals(e.value)) {
                    return true;
                }
            }
        }
        return false;
    }

    private boolean containsNullValue() {
        final AirEntry[] tab = table;
        for (int i = 0; i < tab.length; i++) {
            for (AirEntry e = tab[i]; e != null; e = e.next) {
                if (e.value == null) {
                    return true;
                }
            }
        }
        return false;
    }

    @Override
    public Object clone() {
        AirHashMap<K, V> result = null;
        try {
            result = (AirHashMap<K, V>) super.clone();
        } catch (final CloneNotSupportedException e) {
            // assert false;
        }
        result.table = new AirEntry[table.length];
        result.entrySet = null;
        result.modCount = 0;
        result.size = 0;
        result.init();
        result.putAllForCreate(this);

        return result;
    }

    void addEntry(final int hash, final K key, final V value, final int bucketIndex) {
        final AirEntry<K, V> e = table[bucketIndex];
        table[bucketIndex] = new AirEntry<K, V>(hash, key, value, e);
        if (size++ >= threshold) {
            resize(2 * table.length);
        }
    }

    void createEntry(final int hash, final K key, final V value, final int bucketIndex) {
        final AirEntry<K, V> e = table[bucketIndex];
        table[bucketIndex] = new AirEntry<K, V>(hash, key, value, e);
        size++;
    }

    private final class ValueIterator extends HashIterator<V> {
        @Override
        public V next() {
            return nextEntry().value;
        }
    }

    private final class KeyIterator extends HashIterator<K> {
        @Override
        public K next() {
            return nextEntry().getKey();
        }
    }

    private final class EntryIterator extends HashIterator<Map.Entry<K, V>> {
        @Override
        public Map.Entry<K, V> next() {
            return nextEntry();
        }
    }

    // Subclass overrides these to alter behavior of views' iterator() method
    Iterator<K> newKeyIterator() {
        return new KeyIterator();
    }

    Iterator<V> newValueIterator() {
        return new ValueIterator();
    }

    Iterator<Map.Entry<K, V>> newEntryIterator() {
        return new EntryIterator();
    }

    private transient Set<Map.Entry<K, V>> entrySet = null;

    // public Set<K> keySet() {
    // Set<K> ks = keySet;
    // return (ks != null ? ks : (keySet = new KeySet()));
    // }

    // public Collection<V> values() {
    // Collection<V> vs = values;
    // return (vs != null ? vs : (values = new Values()));
    // }

    @Override
    public Set<Map.Entry<K, V>> entrySet() {
        return entrySet0();
    }

    private Set<Map.Entry<K, V>> entrySet0() {
        final Set<Map.Entry<K, V>> es = entrySet;
        return es != null ? es : (entrySet = new EntrySet());
    }

    private final class EntrySet extends AbstractSet<Map.Entry<K, V>> {
        @Override
        public Iterator<Map.Entry<K, V>> iterator() {
            return newEntryIterator();
        }

        @Override
        public boolean contains(final Object o) {
            if (!(o instanceof Map.Entry)) {
                return false;
            }
            final Map.Entry<K, V> e = (Map.Entry<K, V>) o;
            final AirEntry<K, V> candidate = getEntry(e.getKey());
            return candidate != null && candidate.equals(e);
        }

        @Override
        public boolean remove(final Object o) {
            return removeMapping(o) != null;
        }

        @Override
        public int size() {
            return size;
        }

        @Override
        public void clear() {
            AirHashMap.this.clear();
        }
    }

    private void writeObject(final java.io.ObjectOutputStream s) throws IOException {
        final Iterator<Map.Entry<K, V>> i = size > 0 ? entrySet0().iterator() : null;

        // Write out the threshold, loadfactor, and any hidden stuff
        s.defaultWriteObject();

        // Write out number of buckets
        s.writeInt(table.length);

        // Write out size (number of Mappings)
        s.writeInt(size);

        // Write out keys and values (alternating)
        if (i != null) {
            while (i.hasNext()) {
                final Map.Entry<K, V> e = i.next();
                s.writeObject(e.getKey());
                s.writeObject(e.getValue());
            }
        }
    }

    private void readObject(final java.io.ObjectInputStream s) throws IOException, ClassNotFoundException {
        // Read in the threshold, loadfactor, and any hidden stuff
        s.defaultReadObject();

        // Read in number of buckets and allocate the bucket array;
        final int numBuckets = s.readInt();
        table = new AirEntry[numBuckets];

        init(); // Give subclass a chance to do its thing.

        // Read in size (number of Mappings)
        final int size = s.readInt();

        // Read the keys and values, and put the mappings in the AirHashMap
        for (int i = 0; i < size; i++) {
            final K key = (K) s.readObject();
            final V value = (V) s.readObject();
            putForCreate(key, value);
        }
    }

    // These methods are used when serializing HashSets
    int capacity() {
        return table.length;
    }

    float loadFactor() {
        return loadFactor;
    }

    // ====Entry====
    static class AirEntry<K, V> implements Map.Entry<K, V> {
        final K key;
        V value;
        AirEntry<K, V> next;
        final int hash;

        /**
         * Creates new entry.
         */
        AirEntry(final int h, final K k, final V v, final AirEntry<K, V> n) {
            value = v;
            next = n;
            key = k;
            hash = h;
        }

        @Override
        public final K getKey() {
            return key;
        }

        @Override
        public final V getValue() {
            return value;
        }

        @Override
        public final V setValue(final V newValue) {
            final V oldValue = value;
            value = newValue;
            return oldValue;
        }

        @Override
        public final boolean equals(final Object o) {
            if (!(o instanceof Map.Entry)) {
                return false;
            }
            final Map.Entry e = (Map.Entry) o;
            final Object k1 = getKey();
            final Object k2 = e.getKey();
            if (k1 == k2 || k1 != null && k1.equals(k2)) {
                final Object v1 = getValue();
                final Object v2 = e.getValue();
                if (v1 == v2 || v1 != null && v1.equals(v2)) {
                    return true;
                }
            }
            return false;
        }

        @Override
        public final int hashCode() {
            return (key == null ? 0 : key.hashCode()) ^ (value == null ? 0 : value.hashCode());
        }

        @Override
        public final String toString() {
            return getKey() + "=" + getValue();
        }

        /**
         * This method is invoked whenever the value in an entry is overwritten by an invocation of put(k,v) for a key k that's already in the AirHashMap.
         */
        void recordAccess(final AirHashMap<K, V> m) {
        }

        /**
         * This method is invoked whenever the entry is removed from the table.
         */
        void recordRemoval(final AirHashMap<K, V> m) {
        }
    }

    // ====HashIterator====
    private abstract class HashIterator<E> implements Iterator<E> {
        AirEntry<K, V> next; // next entry to return
        int expectedModCount; // For fast-fail
        int index; // current slot
        AirEntry<K, V> current; // current entry

        HashIterator() {
            expectedModCount = modCount;
            if (size > 0) { // advance to first entry
                final AirEntry[] t = table;
                while (index < t.length && (next = t[index++]) == null) {
                    ;
                }
            }
        }

        @Override
        public final boolean hasNext() {
            return next != null;
        }

        final AirEntry<K, V> nextEntry() {
            if (modCount != expectedModCount) {
                throw new ConcurrentModificationException();
            }
            final AirEntry<K, V> e = next;
            if (e == null) {
                throw new NoSuchElementException();
            }

            if ((next = e.next) == null) {
                final AirEntry[] t = table;
                while (index < t.length && (next = t[index++]) == null) {
                    ;
                }
            }
            current = e;
            return e;
        }

        @Override
        public void remove() {
            if (current == null) {
                throw new IllegalStateException();
            }
            if (modCount != expectedModCount) {
                throw new ConcurrentModificationException();
            }
            final Object k = current.key;
            current = null;
            AirHashMap.this.removeEntryForKey(k);
            expectedModCount = modCount;
        }
    }

    public AirEntry<K, V>[] getTable() {
        return table;
    }
}
