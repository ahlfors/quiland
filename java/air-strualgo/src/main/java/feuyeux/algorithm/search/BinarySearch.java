package feuyeux.algorithm.search;

/**
 * @author feuyeux@gmail.com 2011-3-12
 */
public class BinarySearch {

    public static void main(final String[] args) {
        final int[] orderedInts = new int[] { 1, 3, 5, 7, 9, 10 };
        final BinarySearch binarySearch = new BinarySearch();
        int position = binarySearch.search(orderedInts, 6);
        System.out.println("search 6: " + position);
        position = binarySearch.search(orderedInts, 7);
        System.out.println("search 7: " + position);

    }

    public int search(final int[] orderedInts, final int key) {
        return search0(orderedInts, 0, orderedInts.length - 1, key);
    }

    public int search0(final int[] ints, int low, int high, final int key) {
        while (low <= high) {
            final int mid = low + high >>> 1;
            final int midValue = ints[mid];

            if (midValue < key) {
                low = mid + 1;
            } else if (ints[mid] > key) {
                high = mid - 1;
            } else {
                return mid;
            }
        }
        return -(low + 1);
    }
}
