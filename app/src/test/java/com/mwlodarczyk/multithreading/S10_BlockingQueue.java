package com.mwlodarczyk.multithreading;

import org.junit.Test;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

public class S10_BlockingQueue {

    private static final String EXIT_MSG = "Exit";

    public class Producer implements Runnable {

        private BlockingQueue<String> queue;

        public Producer(BlockingQueue<String> q) {
            this.queue = q;
        }

        @Override
        public void run() {

            for (int i = 0; i < 10; i++) {
                String msg = Integer.toString(i);
                try {
                    Thread.sleep(100);
                    queue.put(msg);
                    System.out.println("Produced " + msg);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }

            try {
                Thread.sleep(500);
                queue.put(EXIT_MSG);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

        }

    }

    public class Consumer implements Runnable {

        private BlockingQueue<String> queue;

        public Consumer(BlockingQueue<String> q) {
            this.queue = q;
        }

        @Override
        public void run() {
            try {
                String msg;

                while ((msg = queue.take()) != EXIT_MSG) {
                    Thread.sleep(10);
                    System.out.println("Consumed " + msg);
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }


    @Test
    public void blockingQueue() throws InterruptedException {

        BlockingQueue<String> queue = new ArrayBlockingQueue<>(10);
        Producer producer = new Producer(queue);
        Consumer consumer = new Consumer(queue);

        Thread producerThread = new Thread(producer);
        Thread consumerThread = new Thread(consumer);

        producerThread.start();
        consumerThread.start();

        producerThread.join();
        consumerThread.join();
    }

}
