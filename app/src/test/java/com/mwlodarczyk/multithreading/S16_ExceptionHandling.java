package com.mwlodarczyk.multithreading;

import org.junit.Test;

public class S16_ExceptionHandling {

    @Test
    public void exceptionHandling() throws InterruptedException {

        Thread.UncaughtExceptionHandler exceptionHandler = new Thread.UncaughtExceptionHandler() {
            public void uncaughtException(Thread th, Throwable ex) {
                System.out.println("Uncaught exception: " + ex);
            }
        };

        Thread thread = new Thread() {
            public void run() {
                System.out.println("Sleeping ...");
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    System.out.println("Interrupted.");
                }
                System.out.println("Throwing exception ...");
                throw new RuntimeException();
            }
        };

        thread.setUncaughtExceptionHandler(exceptionHandler);
        thread.start();
        thread.join();
    }

}
