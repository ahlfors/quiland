package feuyeux.strualgo.sort.jdk;

import java.util.Arrays;
import java.util.Comparator;

/**
 *
 * Created by feuyeux@gmail.com
 * Date: Feb 19 2014
 * Time: 11:43 AM
 */
public class Jdk_1_6MergeSort {
    private static final int INSERTIONSORT_THRESHOLD = 7;
    private static Comparator c;

    public static void legacyMergeSort(Object[] a) {
        c = null;
        Object[] aux = a.clone();
        mergeSort(aux, a, 0, a.length, 0);
    }

    public static void legacyMergeSort(Object[] a, Comparator c0) {
        c = c0;
        Object[] aux = a.clone();
        mergeSort(aux, a, 0, a.length, 0);
    }

    public static void legacyMergeSort(Object[] a, int fromIndex, int toIndex) {
        rangeCheck(a.length, fromIndex, toIndex);
        Object[] aux = Arrays.copyOfRange(a, fromIndex, toIndex);
        mergeSort(aux, a, fromIndex, toIndex, -fromIndex);
    }

    private static void mergeSort(Object[] src, Object[] dest, int low, int high, int off) {
        int length = high - low;
        if (insertionSort(dest, low, high, length))
            return;

        // Recursively sort halves of dest into src
        int destLow = low;
        int destHigh = high;
        low += off;
        high += off;
        int mid = (low + high) >>> 1;
        mergeSort(dest, src, low, mid, -off);
        mergeSort(dest, src, mid, high, -off);

        // If list is already sorted, just copy from src to dest.  This is an
        // optimization that results in faster sorts for nearly ordered lists.
        if (!gt(src[mid - 1], src[mid])) {
            System.arraycopy(src, low, dest, destLow, length);
            return;
        }

        // Merge sorted halves (now in src) into dest
        for (int i = destLow, p = low, q = mid; i < destHigh; i++) {
            if (q >= high || p < mid && !gt(src[p], src[q]))
                dest[i] = src[p++];
            else
                dest[i] = src[q++];
        }
    }

    private static boolean insertionSort(Object[] dest, int low, int high, int length) {
        // Insertion sort on smallest arrays
        if (length < INSERTIONSORT_THRESHOLD) {
            for (int i = low; i < high; i++)
                for (int j = i; j > low && gt(dest[j - 1], dest[j]); j--)
                    swap(dest, j, j - 1);
            return true;
        }
        return false;
    }

    private static boolean gt(Object x, Object y) {
        if (c == null) {
            return ((Comparable) x).compareTo(y) > 0;
        } else {
            return c.compare(x, y) > 0;
        }
    }

    private static void rangeCheck(int length, int fromIndex, int toIndex) {
        if (fromIndex > toIndex) {
            throw new IllegalArgumentException("fromIndex(" + fromIndex + ") > toIndex(" + toIndex + ")");
        }
        if (fromIndex < 0) {
            throw new ArrayIndexOutOfBoundsException(fromIndex);
        }
        if (toIndex > length) {
            throw new ArrayIndexOutOfBoundsException(toIndex);
        }
    }

    private static void swap(Object[] x, int a, int b) {
        Object t = x[a];
        x[a] = x[b];
        x[b] = t;
    }
}
