package feuyeux.datastructure.link.test;

import java.util.ArrayList;

public class ListTester {
    public static void main(final String[] args) {

        final ArrayList<Integer> certs = new ArrayList<Integer>();
        certs.add(9999993);
        certs.add(9999990);
        certs.add(9999992);
        certs.add(9999991);
        final int clen = certs.size();
        for (int i = 0; i < clen; i++) {
            final Integer parent = certs.get(i);
            for (int j = i + 1; j < clen; j++) {
                final Integer son = certs.get(j);
                if (parent - 1 == son) {
                    if (j != i + 1) {
                        final Integer other = certs.get(i + 1);
                        certs.set(i + 1, son);
                        certs.set(j, other);
                    }
                    continue;
                }
            }
        }

        for (final Integer integer : certs) {
            System.out.println(integer);
        }

        certs.add(0, 0);
        certs.add(0, 1);

        for (final Integer integer : certs) {
            System.out.println(integer);
        }
    }
}
