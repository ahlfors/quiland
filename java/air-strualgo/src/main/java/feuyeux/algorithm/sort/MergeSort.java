package feuyeux.algorithm.sort;

/**
 * Merge Sort Implementation
 * @author feuyeux@gmail.com
 * 2010-12-31
 * 
 * need the third array
 * O(n * log n)
 */
public class MergeSort extends Sort {
    public MergeSort(final boolean descending) {
        super(descending);
    }

    private int[] merge(final int[] number, final int temporary[], final int low, final int middle, final int high) {
        int i = low, j = middle, k = low;

        while (i < middle && j < high) {
            stepEcho("->\t", number, number[i], number[j]);
            if (compare(number[i], number[j])) {
                temporary[k++] = number[i++];
            } else {
                temporary[k++] = number[j++];
            }
        }

        while (i < middle) {
            temporary[k++] = number[i++];
        }
        while (j < high) {
            temporary[k++] = number[j++];
        }
        for (i = low; i < high; i++) {
            number[i] = temporary[i];
        }
        return number;
    }

    @Override
    public int[] sort(final int[] number) {
        roundEcho("Merge Sort:\n\t", number);
        final int length = number.length;
        final int[] temporary = new int[length]; // scratch space
        for (int index = 1; index < length; index *= 2) {
            final int x = 2 * index;
            for (int low = 0; low < length; low += x) {
                if (low + x <= length) {
                    merge(number, temporary, low, low + index, low + x);
                } else if (low + index < length) {
                    merge(number, temporary, low, low + index, length);
                }
            }
            roundEcho("{" + index + "}\t", number);
        }
        return number;
    }
}
