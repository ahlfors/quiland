package feuyeux.algorithm.sort;

/**
 * Bubble Sort Implementation
 * @author feuyeux@gmail.com
 * 2010-12-31
 */
public class BubbleSort extends Sort {
    private final boolean traditional = true;

    public BubbleSort(final boolean descending) {
        super(descending);
    }

    @Override
    public int[] sort(final int[] number) {
        if (traditional) {
            return sort0(number);
        }
        return sort1(number);
    }

    /**
     * run in waves from left to right
     */
    public int[] sort0(final int[] number) {
        roundEcho("Bubble Sort:\n\t", number);

        for (int i = number.length; i > 0; i--) {
            for (int j = 1; j < i; j++) {
                final int sentinel = number[j];
                stepEcho("->\t", number, number[j - 1], sentinel);
                if (!compare(number[j - 1], sentinel)) {
                    number[j] = number[j - 1];
                    number[j - 1] = sentinel;
                }
            }
            roundEcho("{" + (number.length - i + 1) + "}\t", number);
        }
        return number;
    }

    /**
     * Odd-Even Transposition Sort
     */
    public int[] sort1(final int[] number) {
        for (int r = 0; r < number.length; r++) {
            for (int i = r & 1; i + 1 < number.length; i += 2) {
                if (compare(number[i + 1], number[i])) {
                    final int x = number[i];
                    number[i] = number[i + 1];
                    number[i + 1] = x;
                }
            }
        }
        return number;
    }
}
