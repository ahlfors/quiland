package feuyeux.algorithm.recursive;

import java.util.ArrayList;

public class Fibonacci2 {
    public static void main(final String[] args) {
        int b1 = 0;
        int b2 = 1;
        final ArrayList<Integer> list = new ArrayList<Integer>();
        list.add(b1);
        list.add(b2);
        for (int i = 3; i < 10; i++) {
            final int b = b1 + b2;
            b1 = b2;
            b2 = b;
            list.add(b);
        }
        for (final Integer i : list) {
            System.out.println(i);
        }
    }
}
