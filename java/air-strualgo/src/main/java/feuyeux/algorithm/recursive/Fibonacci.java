package feuyeux.algorithm.recursive;

import java.util.HashMap;
import java.util.Map;

public class Fibonacci {
    public static int times = 0;

    /**
     * dynamic programming
     */
    private static int[] d;

    private static Map<Integer, Integer> map;

    private static boolean muted = true;

    static {
        Fibonacci.map = new HashMap<Integer, Integer>();
        Fibonacci.map.put(0, 1);
        Fibonacci.map.put(1, 1);
    }

    public static void main(final String[] args) {
        final int n = 11;

        Fibonacci.times = 0;
        System.out.print(Fibonacci.f(n));
        timelog("f");

        Fibonacci.times = 0;
        System.out.print(Fibonacci.f1(n));
        timelog("f1");

        Fibonacci.times = 0;
        System.out.print(Fibonacci.f2(n));
        timelog("f2");

        Fibonacci.times = 0;
        System.out.print(Fibonacci.f3(n));
        timelog("f3");
    }

    public static int f(final int n) {
        plusTime(n);
        if (n <= 1) {
            return 1;
        }
        return f(n - 1) + f(n - 2);
    }

    public static int f1(final int n) {
        Fibonacci.d = new int[n + 1];
        for (int j = 2; j <= n; j++) {
            Fibonacci.d[j] = -1;
        }
        Fibonacci.d[0] = 1;
        Fibonacci.d[1] = 1;
        return f11(n);
    }

    public static int f11(final int n) {
        plusTime(n);
        if (Fibonacci.d[n - 1] < 0) {
            Fibonacci.d[n - 1] = f11(n - 1); // adds "memory"
        }
        return Fibonacci.d[n - 1] + Fibonacci.d[n - 2];
    }

    public static int f2(final int n) {
        plusTime(n);
        final Integer y = Fibonacci.map.get(n - 1);
        if (null == y) {
            Fibonacci.map.put(n - 1, f2(n - 1));
        }
        return Fibonacci.map.get(n - 1) + Fibonacci.map.get(n - 2);
    }

    private static int f3(final int n) {
        final int b1 = 0, b2 = 1;
        return f33(b1, b2, n, 2);
    }

    private static int f33(int b1, int b2, final int n, int i) {
        final int b = b1 + b2;
        plusTime(b);
        if (n >= i) {
            b1 = b2;
            b2 = b;
            return f33(b1, b2, n, ++i);
        }
        return b;
    }

    private static void plusTime(final int n) {
        Fibonacci.times++;
        if (Fibonacci.muted) {
            return;
        }
        System.out.println(n);
    }

    private static void timelog(final String algorithmMethod) {
        System.out.println("\t" + algorithmMethod + " times: " + Fibonacci.times);
    }
}
