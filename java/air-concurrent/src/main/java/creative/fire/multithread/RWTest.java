package creative.fire.multithread;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.ReentrantReadWriteLock;

/**
 * Author: Eric Han
 * Date:   14-8-31
 */
public class RWTest {
    private static ReentrantReadWriteLock lock = new ReentrantReadWriteLock();
    private static ReentrantReadWriteLock.WriteLock wLock = lock.writeLock();
    private static ReentrantReadWriteLock.ReadLock rLock = lock.readLock();
    private static Map<Integer, String> maps = new HashMap<>(2);

    public static void main(String[] args) {
        for (int i = 0; i < 4; i++) {
            new Thread(new ReadThread("r-" + i)).start();
        }
        for (int i = 0; i < 2; i++) {
            new Thread(new WriteThread("w-" + i)).start();
        }
    }

    static class WriteThread implements Runnable {
        private String name;

        public WriteThread(String name) {
            this.name = name;
        }

        @Override
        public void run() {
            while (true) {
                try {
                    wLock.lock();
                    maps.put(1, this.name);
                    System.out.println(this.name + " put and get =" + maps.get(1));
                    TimeUnit.MILLISECONDS.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } finally {
                    wLock.unlock();
                }
                try {
                    TimeUnit.MILLISECONDS.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    static class ReadThread implements Runnable {
        private String name;

        public ReadThread(String name) {
            this.name = name;
        }

        @Override
        public void run() {
            while (true) {
                try {
                    rLock.lock();
                    System.out.println(this.name + " get =" + maps.get(1));
                    TimeUnit.MILLISECONDS.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } finally {
                    rLock.unlock();
                }
                try {
                    TimeUnit.MILLISECONDS.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}