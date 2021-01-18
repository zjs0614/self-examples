package com.zjs.selfstudy.lock;

import java.io.Closeable;
import java.io.IOException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class TryWithLock implements Closeable {
    Lock lock;

    public void close() throws IOException {
        if (lock != null) {
            lock.unlock();
        }
    }

    public TryWithLock(Lock lock) {
        this.lock = lock;
        this.lock.lock();
    }

    public static void main(String[] args) {
        ExecutorService executor = Executors.newFixedThreadPool(2);
        Lock lock = new ReentrantLock();

        executor.submit(() -> {
            try {
                try (TryWithLock tryWithLock = new TryWithLock(lock)) {
                    System.out.println("Start: A");
                    Thread.sleep(3000);
                    System.out.println("Finish: A");
                }
            } catch (IOException e) {
                e.printStackTrace();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        });

        executor.submit(() -> {
            try {
                try (TryWithLock tryWithLock = new TryWithLock(lock)) {
                    System.out.println("Start: B");
                    Thread.sleep(3000);
                    System.out.println("Finish: B");
                }
            } catch (IOException e) {
                e.printStackTrace();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        });
        executor.shutdown();
    }
}
