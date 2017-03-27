package com.mwlodarczyk.multithreading;

import org.junit.Test;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

//Listeners that are fired with a lock held.
//In these cases, it's really easy to get inverted
//Locking between two threads.
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

            System.out.println(name + " try to release first lock");
            firstLock.unlock();

            System.out.println(name + " set second lock");
            secondLock.lock();

            work();
            System.out.println(name + " finished work (part 2)");

            System.out.println(name + " try to release second lock");
            secondLock.unlock();

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

    class Downloader extends Thread {
        private InputStream in;
        private OutputStream out;
        private ArrayList<ProgressListener> listeners;

        public Downloader(URL url, String outputFilename) throws IOException {
            in = url.openConnection().getInputStream();
            out = new FileOutputStream(outputFilename);
            listeners = new ArrayList<>();
        }

        public synchronized void addListener(ProgressListener listener) {
            listeners.add(listener);
        }

        public synchronized void removeListener(ProgressListener listener) {
            listeners.remove(listener);
        }

        private synchronized void updateProgress(int n) {
            for (ProgressListener listener : listeners)
                listener.onProgress(n);
        }

        public void run() {
            int n;
            int total = 0;
            byte[] buffer = new byte[1024];
            try {
                while ((n = in.read(buffer)) != -1) {
                    out.write(buffer, 0, n);
                    total += n;
                    updateProgress(total);
                }
                out.flush();
            } catch (IOException e) {
                System.out.println("IO Exception: " + e.getMessage());
            }
        }
    }

    interface ProgressListener {
        void onProgress(int progress);
    }

}
