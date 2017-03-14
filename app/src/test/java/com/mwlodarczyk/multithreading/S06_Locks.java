package com.mwlodarczyk.multithreading;

import org.junit.Test;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class S06_Locks {

    private int counter = 0;
    private Lock counterLock = new ReentrantLock();

    private void lock() {
        counterLock.lock();
        //counterLock.lockInterruptibly();
        //lock.tryLock();
        //lock.tryLock(1000, TimeUnit.MILLISECONDS);
    }

    private void unlock() {
        counterLock.unlock();
    }

    private void incrementAndPrint() {

        counter++;
        System.out.println("Counter: " + counter + " at: " + Thread.currentThread().getName());

        try {
            Thread.sleep(500);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    class Worker implements Runnable {

        int iterations = 0;

        @Override
        public void run() {
            lock();

            while (iterations < 5) {
                incrementAndPrint();
                iterations++;
            }

            unlock();
        }
    }

    @Test
    public void locks() throws InterruptedException {
        Thread thread = new Thread(new Worker(), "Thread 1");
        Thread secondThread = new Thread(new Worker(), "Thread 2");

        thread.start();
        secondThread.start();

        thread.join();
        secondThread.join();
    }

}
