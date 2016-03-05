package feuyeux.datastructure.heap;

/**
 * Created by erichan on 1/7/14.
 */
public class PriorityQueueTest {
    /*
    堆：完全二叉樹
        6
       / \
      10 18
     / \
    23 44

    A[i]的左右兒子 A[2i] A[2i+1] 父結點是A[i/2]
    */
    public static void main(final String[] args) {
        final AirPriorityQueue<Integer> pq = new AirPriorityQueue<>(5);
        /*first time*/
        pq.offer(18);
        pq.offer(44); // see stack trace
        pq.offer(6);
        pq.offer(23);
        pq.offer(10);

        System.out.println();
        final Integer[] es = pq.toArray(new Integer[] {});
        for (final Integer e : es) {
            System.out.print(e + "\t");
        }
        System.out.println();

        while (pq.size() > 0) {
            pq.poll(); //see stack trace
        }
        /*second time*/
        //secondTime(pq);
    }

}
