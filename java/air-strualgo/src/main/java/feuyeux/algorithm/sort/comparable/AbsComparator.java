package feuyeux.algorithm.sort.comparable;

import java.util.Arrays;
import java.util.Comparator;
import java.util.Random;

public class AbsComparator implements Comparator<Integer> {
    @Override
    public int compare(final Integer o1, final Integer o2) {
        final int v1 = Math.abs(o1.intValue());
        final int v2 = Math.abs(o2.intValue());
        return v1 > v2 ? 1 : v1 == v2 ? 0 : -1;
    }

    public static void main(final String[] args) {
        final Integer[] integers = prepareArray();
        Arrays.sort(integers);
        System.out.println(Arrays.asList(integers));

        Arrays.sort(integers, new AbsComparator());
        System.out.println(Arrays.asList(integers));
    }

    /**
     * @return
     */
    private static Integer[] prepareArray() {
        final Random rnd = new Random();
        final Integer[] integers = new Integer[11];
        for (int i = 0; i < integers.length; i++) {
            integers[i] = new Integer(rnd.nextInt(100) * (rnd.nextBoolean() ? 1 : -1));
        }
        return integers;
    }
}
