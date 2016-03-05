package feuyeux.algorithm.sort;

/**
 * Quick Sort Implementation
 * 
 * @author feuyeux@gmail.com 2010-12-31
 */
public class QuickSort1 {
    private void swap(final int[] array, final int i, final int j) {
        if (i == j) {
            return;
        }
        final int tmp = array[i];
        array[i] = array[j];
        array[j] = tmp;
    }

    private int partition(final int[] number, final int begin, final int end) {
        int index = (end - begin + 1) / 2;
        final int pivot = number[index];
        swap(number, index, end);
        for (int i = index = begin; i < end; ++i) {
            if (number[i] > pivot) {
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

    public static void main(final String[] args) {
        final int[] sorted = new QuickSort1().sort(new int[] { 3, 6, 1, 7, 2 });
        for (final int i : sorted) {
            System.out.println(i);
        }
    }

    public int[] sort(final int[] number) {
        return qsort(number, 0, number.length - 1);
    }
}