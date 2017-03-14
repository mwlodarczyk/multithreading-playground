package com.mwlodarczyk.multithreading;

import org.junit.Ignore;
import org.junit.Test;

import java.util.concurrent.atomic.AtomicInteger;

public class S02_OutOfMemory {

    @Test
    @Ignore
    public void lotsOfThreads() {
        final AtomicInteger counter = new AtomicInteger();
        while (true) {
            new Thread(new Runnable() {
                @Override
                public void run() {
                    System.out.println("Counter: " + counter.getAndIncrement());
                    try {
                        Thread.sleep(10_000_000);
                    }
                    catch(InterruptedException e){
                        Thread.currentThread().interrupt();
                    }
                }
            }).start();
        }
    }

}
