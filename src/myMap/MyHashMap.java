package myMap;

import java.util.*;


public class MyHashMap<K, V> implements Map<K, V> {

    static class Node<K,V> implements Map.Entry<K, V> {
        final int hash;
        final K key;
        V value;
        Node<K, V> next;

        Node(int hash, K key, V value, Node<K, V> next) {
            this.hash = hash;
            this.key = key;
            this.value = value;
            this.next = next;
        }

        public K getKey() { return key; }
        public V getValue() { return value; }

        public V setValue(V newValue) {
            V old = value;
            value = newValue;
            return old;
        }
        @Override
        public final int hashCode() {
            return Objects.hashCode(key) ^ Objects.hashCode(value);
        }

        @Override
        public final boolean equals(Object o) {
            if (o == this)
                return true;
            if (o instanceof Map.Entry<?, ?> e) {
                return Objects.equals(key, e.getKey()) &&
                        Objects.equals(value, e.getValue());
            }
            return false;
        }
    }

    static final int DEFAULT_INITIAL_CAPACITY = 16;
    static final float DEFAULT_LOAD_FACTOR = 0.75f;

    Node<K, V>[] table;
    int size;
    int threshold;
    final float loadFactor;

    @SuppressWarnings("unchecked")
    public MyHashMap() {
        this.loadFactor = DEFAULT_LOAD_FACTOR;
        this.threshold = (int)(DEFAULT_INITIAL_CAPACITY * loadFactor);
        this.table = new Node[DEFAULT_INITIAL_CAPACITY];
    }

    static final int hash(Object key) {
        int h;
        return (key == null) ? 0 : (h = key.hashCode()) ^ (h >>> 16);
    }

    final Node<K, V> getNode(Object key) {
        int h = hash(key);
        int index = (table.length - 1) & h;
        Node<K, V> e = table[index];

        while (e != null) {
            if (e.hash == h && Objects.equals(e.key, key))
                return e;
            e = e.next;
        }
        return null;
    }

    @Override
    public V get(Object key) {
        Node<K, V> node = getNode(key);
        return node == null ? null : node.value;
    }

    @Override
    public V put(K key, V value) {
        return putVal(hash(key), key, value);
    }

    final V putVal(int hash, K key, V value) {
        int index = (table.length - 1) & hash;
        Node<K, V> first = table[index];

        for (Node<K, V> e = first; e != null; e = e.next) {
            if (e.hash == hash && Objects.equals(e.key, key)) {
                V old = e.value;
                e.value = value;
                return old;
            }
        }

        table[index] = new Node<>(hash, key, value, first);
        size++;

        if (size > threshold)
            resize();

        return null;
    }

    @SuppressWarnings("unchecked")
    final void resize() {
        Node<K, V>[] oldTable = table;
        int oldCap = oldTable.length;
        int newCap = oldCap << 1;
        threshold = (int)(newCap * loadFactor);

        Node<K, V>[] newTable = new Node[newCap];
        table = newTable;

        for (Node<K, V> e : oldTable) {
            while (e != null) {
                Node<K, V> next = e.next;
                int index = (newCap - 1) & e.hash;
                e.next = newTable[index];
                newTable[index] = e;
                e = next;
            }
        }
    }

    @Override
    public V remove(Object key) {
        int h = hash(key);
        int index = (table.length - 1) & h;
        Node<K, V> prev = null;
        Node<K, V> e = table[index];

        while (e != null) {
            Node<K, V> next = e.next;
            if (e.hash == h && Objects.equals(e.key, key)) {
                if (prev == null)
                    table[index] = next;
                else
                    prev.next = next;

                size--;
                return e.value;
            }
            prev = e;
            e = next;
        }
        return null;
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
    public Set<Entry<K, V>> entrySet() {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean containsKey(Object key) {
        return getNode(key) != null;
    }

    @Override
    public boolean containsValue(Object value) {
        for (Node<K,V> bucket : table) {
            while (bucket != null) {
                if (Objects.equals(bucket.value, value))
                    return true;
                bucket = bucket.next;
            }
        }
        return false;
    }

    @Override
    public void clear() {
        Arrays.fill(table, null);
        size = 0;
    }

    @Override
    public void putAll(Map<? extends K, ? extends V> m) {
        throw new UnsupportedOperationException();
    }

    @Override
    public Set<K> keySet() {
        throw new UnsupportedOperationException();
    }

    @Override
    public Collection<V> values() {
        throw new UnsupportedOperationException();
    }


}
