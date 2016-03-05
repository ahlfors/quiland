package creative.fire.concurrent.lock;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.CyclicBarrier;
import java.util.concurrent.locks.ReentrantReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock.ReadLock;
import java.util.concurrent.locks.ReentrantReadWriteLock.WriteLock;

public class RWLockTest {
    private static ReentrantReadWriteLock lock = new ReentrantReadWriteLock();
    private static WriteLock wLock = lock.writeLock();
    private static ReadLock rLock = lock.readLock();
    private static Map<Integer, String> maps = new HashMap<>(2);
    private static CountDownLatch latch = new CountDownLatch(102);
    private static CyclicBarrier barrier = new CyclicBarrier(102);

    static class WriteThread implements Runnable {
        public WriteThread(String name) {
            Thread.currentThread().setName(name);
        }

        @Override
        public void run() {
            try {
                barrier.await();
            } catch (InterruptedException | BrokenBarrierException e) {
                e.printStackTrace();
            }
            wLock.lock();
            maps.put(1, Thread.currentThread().getName());
            System.out.println(Thread.currentThread().getName() + " put and get =" + maps.get(1));
            try {
                Thread.sleep(50);
            } catch (InterruptedException e) {
                e.printStackTrace();
            } finally {
                wLock.unlock();
            }
            latch.countDown();
        }
    }

    static class ReadThread implements Runnable {
        public ReadThread(String name) {
            Thread.currentThread().setName(name);
        }

        @Override
        public void run() {
            try {
                barrier.await();
            } catch (InterruptedException | BrokenBarrierException e) {
                e.printStackTrace();
            }

            rLock.lock();
            System.out.println(Thread.currentThread().getName() + " get =" + maps.get(1));
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                e.printStackTrace();
            } finally {
                rLock.unlock();
            }
            latch.countDown();
        }
    }

    public static void main(String[] args) throws Exception {
        long begin = System.currentTimeMillis();

        for (int i = 0; i < 100; i++) {
            new Thread(new ReadThread("r-" + i)).start();
        }
        for (int i = 0; i < 2; i++) {
            new Thread(new WriteThread("w-" + i)).start();
        }
        latch.await();
        long end = System.currentTimeMillis();
        System.out.println("Consume Time is: " + (end - begin) + " ms");
    }

}
