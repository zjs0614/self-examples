package com.zjs.selfstudy.synchronization;

import lombok.NoArgsConstructor;

import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.atomic.AtomicInteger;

@NoArgsConstructor
public class CAS_Transaction {

    AtomicInteger count =  new AtomicInteger(0);

    public void increase() {
        boolean updated = false;

        while (!updated) {
            int oldCount = this.count.get();
            updated = this.count.compareAndSet(oldCount, oldCount+1);
        }

        System.out.println(this.count.get());
    }

    public static void main(String args[]) {
        CAS_Transaction cas_transaction = new CAS_Transaction();

        ThreadPoolExecutor executor = (ThreadPoolExecutor) Executors.newFixedThreadPool(1000);

        for (int i=0; i<10000; i++) {
            executor.submit(() -> {
                try {
                    Thread.sleep(1000);
                    cas_transaction.increase();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            });
        }

    }

}
