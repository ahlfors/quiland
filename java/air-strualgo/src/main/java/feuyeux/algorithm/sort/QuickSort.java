package feuyeux.algorithm.sort;

import java.util.Random;

/**
 * Quick Sort Implementation
 * 
 * @author feuyeux@gmail.com 2010-12-31
 */
public class QuickSort extends Sort {
    public QuickSort(final boolean descending) {
        super(descending);
    }

    private final Random RND = new Random();

    private void swap(final int[] array, final int i, final int j) {
        if (i == j) {
            return;
        }
        if (!Sort.muted) {
            System.out.println("swap:" + array[i] + "," + array[j]);
        }
        final int tmp = array[i];
        array[i] = array[j];
        array[j] = tmp;
    }

    private int partition(final int[] number, final int begin, final int end) {
        int index = begin + RND.nextInt(end - begin + 1);
        if (!Sort.muted) {
            System.out.println("\nprocess [&" + begin + " ~ &" + end + "]");
            System.out.println("pivot: &" + index);
        }
        final int pivot = number[index];
        swap(number, index, end);
        for (int i = index = begin; i < end; ++i) {
            stepEcho("->\t", number, number[i], pivot);
            if (compare(number[i], pivot)) {
                swap(number, index++, i);
            }
        }
        swap(number, index, end);
        return index;
    }

    private int[] qsort(final int[] array, final int begin, final int end) {
        if (end > begin) {
            final int pivot = partition(array, begin, end);
            qsort(array, begin, pivot - 1);
            qsort(array, pivot + 1, end);
        }
        return array;
    }

    @Override
    public int[] sort(final int[] number) {
        roundEcho("Quick Sort:\n\t", number);
        return qsort(number, 0, number.length - 1);
    }
}