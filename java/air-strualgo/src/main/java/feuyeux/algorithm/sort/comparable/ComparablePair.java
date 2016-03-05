/**
 * 
 */
package feuyeux.algorithm.sort.comparable;

import java.util.ArrayList;
import java.util.Arrays;

/**
 * @author luh
 * 
 */
public class ComparablePair implements Comparable<ComparablePair> {
    private String id;
    private int value;

    public ComparablePair(final String id, final int value) {
        this.id = id;
        this.value = value;
    }

    @Override
    public int compareTo(final ComparablePair sdgDistance) {
        if (sdgDistance.getValue() > value) {
            return -1;
        }
        if (sdgDistance.getValue() < value) {
            return 1;
        }
        return 0;
    }

    public String getId() {
        return id;
    }

    public void setId(final String id) {
        this.id = id;
    }

    public int getValue() {
        return value;
    }

    public void setValue(final int value) {
        this.value = value;
    }

    public static void main(final String[] args) {
        final ArrayList<ComparablePair> compas = new ArrayList<ComparablePair>();
        compas.add(new ComparablePair("2", 126));
        compas.add(new ComparablePair("5", 123));
        compas.add(new ComparablePair("7", 129));
        compas.add(new ComparablePair("9", 23));

        final ComparablePair[] acompa = compas.toArray(new ComparablePair[] {});
        Arrays.sort(acompa);

        for (final ComparablePair compa : acompa) {
            System.out.println(compa.getId());
        }
    }
}
