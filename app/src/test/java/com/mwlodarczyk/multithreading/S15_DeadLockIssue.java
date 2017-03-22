package com.mwlodarczyk.multithreading;

import org.junit.Test;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class S15_DeadLockIssue {

    class WorkerThread implements Runnable {

        private Lock firstLock;
        private Lock secondLock;

        public WorkerThread(Lock firstLock, Lock secondLock) {
            this.firstLock = firstLock;
            this.secondLock = secondLock;
        }

        @Override
        public void run() {

            String name = Thread.currentThread().getName();
            System.out.println(name + " started");

            System.out.println(name + " set first lock");
            firstLock.lock();

            work();
            System.out.println(name + " finished work (part 1)");

            System.out.println(name + " set second lock");
            secondLock.lock();

            work();
            System.out.println(name + " finished work (part 2)");

            System.out.println(name + " try to release second lock");
            secondLock.unlock();

            System.out.println(name + " try to release first lock");
            firstLock.unlock();

            System.out.println(name + " finished");
        }

        private void work() {
            try {
                Thread.sleep(5000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    @Test
    public void deadLockIssue() throws InterruptedException {

        Lock firstLock = new ReentrantLock();
        Lock secondLock = new ReentrantLock();
        Lock thirdLock = new ReentrantLock();

        Thread firstThread = new Thread(new WorkerThread(firstLock, secondLock), "Thread_1");
        Thread secondThread = new Thread(new WorkerThread(secondLock, thirdLock), "Thread_2");
        Thread thirdThread = new Thread(new WorkerThread(thirdLock, firstLock), "Thread_3");

        firstThread.start();
        Thread.sleep(1000);
        secondThread.start();
        Thread.sleep(1000);
        thirdThread.start();

        firstThread.join();
        secondThread.join();
        thirdThread.join();
    }

}
