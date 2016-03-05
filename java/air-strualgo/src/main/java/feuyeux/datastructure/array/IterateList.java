package feuyeux.datastructure.array;

import java.util.ArrayList;
import java.util.concurrent.CountDownLatch;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author hanl
 */
public class IterateList {

    static final Logger logger = Logger.getLogger(IterateList.class.getName());
    static ArrayList<Integer> list = new ArrayList<Integer>();
    static final CountDownLatch gateLatch1 = new CountDownLatch(1);
    static final CountDownLatch gateLatch2 = new CountDownLatch(1);

    public void addMock() {
        int top = 0;
        while (top < 20) {
            IterateList.list.add(top++);
        }
    }

    public void iterating1() {
        try {
            //java.lang.IndexOutOfBoundsException:
            //for (int i = 0, len =list.size(); i < len; i++) {
            for (int i = 0; i < IterateList.list.size(); i++) {
                if (i == 3) {
                    IterateList.gateLatch1.countDown();
                }
                IterateList.logger.log(Level.INFO, "i={0} list({1})='{'{2}'}'", new Object[] { i, i, IterateList.list.get(i) });
            }
        } catch (final Exception e) {
            IterateList.logger.log(Level.SEVERE, null, e);
        }
    }

    public void iterating2() {
        try {
            int i = 0;
            //java.util.ConcurrentModificationException:
            for (final int n : IterateList.list) {
                if (i == 3) {
                    IterateList.gateLatch2.countDown();
                }
                IterateList.logger.log(Level.INFO, "i={0} list({1})='{'{2}'}'", new Object[] { i, i, n });
                i++;
            }
        } catch (final Exception e) {
            IterateList.logger.log(Level.SEVERE, null, e);
        }
    }

    public static void main(final String[] ss) {
        final IterateList test = new IterateList();
        final Thread remover1 = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    IterateList.gateLatch1.await();
                } catch (final InterruptedException ex) {
                    IterateList.logger.log(Level.SEVERE, null, ex);
                }
                if (!IterateList.list.isEmpty()) {
                    IterateList.list.remove(0);
                }

                IterateList.logger.log(Level.INFO, "list size:{0}", IterateList.list.size());
            }
        });
        final Thread remover2 = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    IterateList.gateLatch2.await();
                } catch (final InterruptedException ex) {
                    IterateList.logger.log(Level.SEVERE, null, ex);
                }
                if (!IterateList.list.isEmpty()) {
                    IterateList.list.remove(0);
                }
                IterateList.logger.log(Level.INFO, "list size:{0}", IterateList.list.size());
            }
        });
        remover1.start();
        remover2.start();
        test.addMock();
        test.iterating1();
        test.iterating2();
    }
}