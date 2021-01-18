package com.zjs.selfstudy.lock;

import java.io.IOException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class FunctionLock extends ReentrantLock {

    @FunctionalInterface
    interface FunctionLockInterface {
        void lock();
    }

    public FunctionLock() {
        super();
    }

    public void tryWithLock(FunctionLockInterface functionLockInterface) {
        super.lock();
        try {
            functionLockInterface.lock();
        } catch (Throwable t) {
            throw t;
        } finally {
            super.unlock();
        }
    }

    public static void main(String[] args) {
        ExecutorService executor = Executors.newFixedThreadPool(2);

        FunctionLock functionLock = new FunctionLock();

        executor.submit(() -> {
            functionLock.tryWithLock(() -> {
                try {
                    System.out.println("Start: A");
                    Thread.sleep(3000);
                    System.out.println("Finish: A");
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            });
        });

        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        executor.submit(() -> {
            functionLock.tryWithLock(() -> {
                try {
                    System.out.println("Start: B");
                    Thread.sleep(3000);
                    System.out.println("Finish: B");
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            });
        });
        executor.shutdown();
    }
}
