package feuyeux.algorithm.sort;

/**
 * 选择排序法-----堆排（不稳定）
 */
public class HeapSort extends Sort {
    public HeapSort(final boolean descending) {
        super(descending);
    }

    @Override
    public int[] sort(final int[] number) {
        final int[] tmp = new int[number.length + 1]; // 配合說明，使用一個有徧移的暫存陣列
        for (int i = 1; i < tmp.length; i++) {
            tmp[i] = number[i - 1];
        }
        createHeap(tmp);
        int m = number.length;
        while (m > 1) {
            swap(tmp, 1, m);
            m--;
            int p = 1;
            int s = 2 * p;
            while (s <= m) {
                if (s < m && tmp[s + 1] < tmp[s]) {
                    s++;
                }
                if (tmp[p] <= tmp[s]) {
                    break;
                }
                swap(tmp, p, s);
                p = s;
                s = 2 * p;
            }
        } // 這邊將排序好的暫存陣列設定回原陣列
        for (int i = 0; i < number.length; i++) {
            number[i] = tmp[i + 1];
        }
        return number;
    }

    private void createHeap(final int[] tmp) {
        final int[] heap = new int[tmp.length];
        for (int i = 0; i < heap.length; i++) {
            heap[i] = -1;
        }
        for (int i = 1; i < heap.length; i++) {
            heap[i] = tmp[i];
            int s = i;
            int p = i / 2;
            while (s >= 2 && heap[p] > heap[s]) {
                swap(heap, p, s);
                s = p;
                p = s / 2;
            }
        }
        for (int i = 1; i < tmp.length; i++) {
            tmp[i] = heap[i];
        }
    }

    private void swap(final int[] number, final int i, final int j) {
        int t;
        t = number[i];
        number[i] = number[j];
        number[j] = t;
    }
}