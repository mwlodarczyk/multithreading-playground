package com.mwlodarczyk.multithreading;

import org.junit.Test;

import java.io.IOException;
import java.util.concurrent.Semaphore;

public class S07_Semaphores {

    class ConnectionLimiter {
        private final Semaphore semaphore;

        private ConnectionLimiter(int maxConcurrentRequests) {
            semaphore = new Semaphore(maxConcurrentRequests);
        }

        public void acquire() throws InterruptedException, IOException {
            semaphore.acquire();
        }

        public void release() {
            try {
           /*
           * ... clean up here
           */
            } finally {
                semaphore.release();
            }
        }
    }

    private Thread process(final ConnectionLimiter limiter) {
        return new Thread(new Runnable() {
            @Override
            public void run() {
                try {

                    System.out.println("Acquiring connection for thread: " + Thread.currentThread().getId());
                    limiter.acquire();

                    System.out.println("Processing connection for thread: " + Thread.currentThread().getId());
                    Thread.sleep(5000);

                    limiter.release();
                    System.out.println("Releasing connection for thread: " + Thread.currentThread().getId());

                } catch (InterruptedException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    @Test
    public void semaphores() throws InterruptedException {
        ConnectionLimiter connectionLimiter = new ConnectionLimiter(2);

        Thread firstThread = process(connectionLimiter);
        Thread secondThread = process(connectionLimiter);
        Thread thirdThread = process(connectionLimiter);

        firstThread.start();
        secondThread.start();
        thirdThread.start();

        firstThread.join();
        secondThread.join();
        thirdThread.join();
    }

}

