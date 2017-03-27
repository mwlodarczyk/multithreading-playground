package com.mwlodarczyk.multithreading;

import org.junit.Test;

//Do not use synchronized keyword never ever :)
//Just one exception to explain why it should not be used
public class S04_Synchronization {

    private Object lock;
    private static int counter = 0;

    private void print(){
        synchronized(lock){
            execute();
        }
    }

    private synchronized void altPrint(){
        execute();
    }

    private synchronized static void staticPrint() {
        execute();
    }

    private static void altStaticPrint() {
        synchronized(S04_Synchronization.class) {
            execute();
        }
    }

    private static void notSynchronizedPrint(){
        execute();
    }

    private static final String LOCK = "LOCK";

    public void synchronizedStaticStringLock() {
        synchronized(LOCK) {
            execute();
        }
    }

    private static void execute(){
        for (int i = 0; i <= 10; i++) {
            System.out.println("Printing: " + i + " on thread: " + Thread.currentThread().getId());
            counter++;
        }
    }

    class JobExecutor implements Runnable {
        @Override
        public void run() {
            notSynchronizedPrint();
        }
    }

    @Test
    public void printNumbers() {
        Thread thread = new Thread(new JobExecutor());
        Thread refThread = new Thread(new JobExecutor());

        thread.start();
        refThread.start();

        try {
            thread.join();
            refThread.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

}
