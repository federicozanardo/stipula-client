package lib.datastructures;

import java.io.Serializable;

public class Triple<K, L, V> implements Serializable {
    private final K first;
    private final L second;
    private final V third;

    public Triple(K first, L second, V third) {
        this.first = first;
        this.second = second;
        this.third = third;
    }

    public K getFirst() {
        return first;
    }

    public L getSecond() {
        return second;
    }

    public V getThird() {
        return third;
    }

    @Override
    public String toString() {
        return "Triple{" +
                "first=" + first +
                ", second=" + second +
                ", third=" + third +
                '}';
    }
}
