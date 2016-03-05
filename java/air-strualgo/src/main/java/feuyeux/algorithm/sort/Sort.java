package feuyeux.algorithm.sort;

public abstract class Sort implements ISort {
    private final boolean descending;
    public static boolean muted = false;

    public Sort(final boolean descending) {
        this.descending = descending;
    }

    @Override
    public boolean compare(final int x, final int y) {
        return isDescending() ? x > y : x <= y;
    }

    @Override
    public boolean isDescending() {
        return descending;
    }

    protected void roundEcho(final String message, final int[] loopArray) {
        if (Sort.muted) {
            return;
        }
        System.out.print(message + " ");
        if (loopArray.length == 0) {
            return;
        }
        for (int i = 0; i < loopArray.length; i++) {
            if (i == loopArray.length - 1) {
                System.out.println(loopArray[i]);
            } else {
                System.out.print(loopArray[i] + "\t");
            }
        }
    }

    protected void stepEcho(final String message, final int[] loopArray, final int x, final int y) {
        if (Sort.muted) {
            return;
        }
        System.out.print(message);
        if (loopArray.length == 0) {
            return;
        }
        for (int i = 0; i < loopArray.length; i++) {
            if (loopArray[i] == x || loopArray[i] == y) {
                System.out.print("[" + loopArray[i] + "]" + "\t");
            } else {
                System.out.print(loopArray[i] + "\t");
            }
        }
        System.out.println("[" + x + "," + y + "]");
    }
}
