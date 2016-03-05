package feuyeux.algorithm.sort;

public class CountSort {
    public static void main(final String[] args) {
        int[] a = { 3, 1, 6, 0, 3, 0, 1, 5, 3, 6 };
        final int max = getMax(a);
        print(a, "Before sort:");
        a = Sort(a, max);
        print(a, "After  sort:");
    }

    public static int[] Sort(final int[] a, final int max) {
        final int[] b = new int[a.length];
        final int[] c = new int[max + 1];

        for (int i = 0; i < b.length; i++) {
            b[i] = 0;
        }

        for (int i = 0; i < c.length; i++) {
            c[i] = 0;
        }

        int temp = 0;
        for (int i = 0; i < a.length; i++) {
            temp = a[i];
            c[temp] = c[temp] + 1;
        }

        print(c, "summary");

        for (int i = 1; i < c.length; i++) {
            c[i] = c[i] + c[i - 1];
        }

        print(c, "where2start");

        for (int i = a.length - 1; i >= 0; i--) {
            temp = a[i];
            b[c[temp] - 1] = temp;
            c[temp] = c[temp] - 1;
        }
        return b;
    }

    public static int getMax(final int[] a) {
        int max = a[0];

        for (int i = 1; i < a.length; i++) {
            if (a[i] > max) {
                max = a[i];
            }
        }

        return max;
    }

    public static void print(final int[] a, final String str) {
        System.out.println(str);

        for (int i = 0; i < a.length; i++) {
            System.out.print(a[i] + " ");
        }

        System.out.println();
    }
}