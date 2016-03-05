package feuyeux.strualgo.sort;

import feuyeux.strualgo.sort.comparable.AbsComparator;
import feuyeux.strualgo.sort.jdk.ComparableTimSort;
import feuyeux.strualgo.sort.jdk.DualPivotQuicksort;
import feuyeux.strualgo.sort.jdk.Jdk_1_6MergeSort;
import feuyeux.strualgo.sort.jdk.TimSort;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Sort Algorithm Test Unit
 *
 * @author feuyeux@gmail.com
 * 2010-12-31
 */
public class SortingTest {
    private ISort sorter;
    private int[] finalArray;
    private int[] initialArray;
    private static final int[] desiredArray;
    private static boolean descend = true;

    public static final int[] DESC_ARRAY = {9, 8, 7, 6, 5, 4, 3, 2, 1, 0};
    public static final int[] ASC_ARRAY = {0, 1, 2, 3, 4, 5, 6, 7, 8, 9};

    static {
        if (descend)
            desiredArray = DESC_ARRAY;
        else
            desiredArray = ASC_ARRAY;
        System.out.println("Happy New Year 2011!\nI glad to share it with you all\nfeuyeux@gmail.com\n\n");
    }

    @Before
    public void before() {
        initialArray = new int[]{8, 6, 1, 4, 9, 2, 5, 3, 0, 7};
    }

    @After
    public void after() {

    }

    @Test
    public void testBubbleSort() {
        sorter = new BubbleSort(descend);
        finalArray = sorter.sort(initialArray);
        p("testBubbleSort", finalArray);
        assertArrayEquals(finalArray, desiredArray);
    }

    @Test
    public void testSelectionSort() {
        sorter = new SelectionSort(descend);
        finalArray = sorter.sort(initialArray);
        p("testSelectionSort", finalArray);
        assertArrayEquals(finalArray, desiredArray);
    }

    @Test
    public void testInsertionSort() {
        sorter = new InsertionSort(descend);
        finalArray = sorter.sort(initialArray);
        p("testInsertionSort", finalArray);
        assertArrayEquals(finalArray, desiredArray);
    }

    @Test
    public void testShellSort() {
        sorter = new ShellSort(descend);
        finalArray = sorter.sort(initialArray);
        p("testShellSort", finalArray);
        assertArrayEquals(finalArray, desiredArray);
    }

    @Test
    public void testMergeSort() {
        sorter = new MergeSort(descend);
        finalArray = sorter.sort(initialArray);
        p("testMergeSort", finalArray);
        assertArrayEquals(finalArray, desiredArray);
        sorter.sort(new int[]{38, 27, 43, 3, 9, 82, 10});
    }

    @Test
    public void testJdkSorts() {
        Object[] a = new Object[]{8, 6, 1, 4, 9, 2, 5, 3, 0, 7};
        p("JDK1.6 merge sort", a);
        Jdk_1_6MergeSort.legacyMergeSort(a);
        p("", a);

        a = new Object[]{8, 6, 1, 4, 9, 2, 5, 3, 0, 7};
        p("JDK1.7 tim sort", a);
        ComparableTimSort.sort(a);
        p("", a);

        Integer[] b = new Integer[]{8, 6, 1, 4, 9, 2, 5, 3, 0, 7};
        p("JDK1.7 tim sort", b);
        TimSort.sort(b, new AbsComparator());
        p("", b);

        Sort.muted = true;
        int[] c = new int[]{8, 6, 1, 4, 9, 2, 5, 3, 0, 7};
        p("JDK1.7 dual pivot quick sort", c);
        DualPivotQuicksort.sort(c);
        p("", a);
    }

    private void p(String name, Object[] arr) {
        if (!name.isEmpty())
            System.out.println(name + ":");
        for (int i = 0; i < arr.length; i++) {
            if (arr.length - 1 == i)
                System.out.println(arr[i]);
            else
                System.out.print(arr[i] + "\t");
        }
    }

    @Test
    public void testQuickSort() {
        sorter = new QuickSort(descend);
        finalArray = sorter.sort(initialArray);
        p("testQuickSort", finalArray);
        assertArrayEquals(finalArray, desiredArray);
    }

    private void p(String name, int[] arr) {
        if (!Sort.muted)
            return;
        System.out.println(name + ":");
        for (int i = 0; i < arr.length; i++) {
            if (arr.length - 1 == i)
                System.out.println(arr[i]);
            else
                System.out.print(arr[i] + "\t");
        }
    }
}
