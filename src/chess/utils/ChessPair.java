package chess.utils;

public class ChessPair<K extends Comparable<K>, V> implements Comparable<ChessPair<K, V>> {
    private K key;
    private V value;

    public ChessPair(K key, V value) {
        this.key = key;
        this.value = value;
    }

    public K getKey() {
        return key;
    }

    public V getValue() {
        return value;
    }

    @Override
    public String toString() {
        return key.toString() + " -> " + value.toString();
    }

    @Override
    public int compareTo(ChessPair<K, V> other) {
        return this.key.compareTo(other.getKey());
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ChessPair<?, ?> chessPair = (ChessPair<?, ?>) o;
        return key.equals(chessPair.key);
    }

}