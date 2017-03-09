package com.tomtom.multithreading;

import org.junit.Test;

import java.util.concurrent.atomic.AtomicInteger;

public class S02_OutOfMemory {

    @Test
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
