package com.tomtom.multithreading;

import org.junit.Test;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class S02_EvenOdd {

    private final Lock printLock = new ReentrantLock();

    class PrintRunnable implements Runnable {

        int min = 0;
        int max = 10;

        public PrintRunnable(int min, int max) {
            this.min = min;
            this.max = max;
        }

        @Override
        public void run() {
            printLock.lock();
            for (int i = min; i < max; i++) {
                System.out.println("Printing: " + i + " on thread: " + Thread.currentThread().getId());
            }
            printLock.unlock();
        }
    }

    @Test
    public void printNumbers() {
        Thread thread = new Thread(new PrintRunnable(0, 10));
        Thread refThread = new Thread(new PrintRunnable(11, 20));

        thread.start();
        refThread.start();
        try {
            thread.join();
            refThread.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        System.out.println("Threads finished");
    }

}
