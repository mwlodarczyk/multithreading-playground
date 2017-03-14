package com.mwlodarczyk.multithreading;

import org.junit.Test;

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;


public class S09_EvenOdd {

    class Printer implements Runnable {

        private NumberPrinter numberPrinter;

        Printer(NumberPrinter numberPrinter) {
            this.numberPrinter = numberPrinter;
        }

        public void run() {
            boolean shouldContinue = true;
            while (shouldContinue) {
                try {
                    shouldContinue = numberPrinter.printNumber();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                    return;
                }
            }
        }
    }

    interface NumberPrinter {
        boolean printNumber() throws InterruptedException;
    }

    class OddPrinter implements NumberPrinter {

        private Counter counter;

        OddPrinter(Counter counter) {
            this.counter = counter;
        }

        @Override
        public boolean printNumber() throws InterruptedException {
            return counter.printNextOdd();
        }
    }

    class EvenPrinter implements NumberPrinter {

        private Counter counter;

        EvenPrinter(Counter counter) {
            this.counter = counter;
        }

        @Override
        public boolean printNumber() throws InterruptedException {
            return counter.printNextEven();
        }
    }

    class Counter {

        private int count;
        private int upperLimit;
        private boolean printOdd = true;

        private final ReentrantLock printLock;
        private final Condition oddCondition;
        private final Condition evenCondition;

        Counter(int limit) {
            upperLimit = limit;
            printLock = new ReentrantLock();
            oddCondition = printLock.newCondition();
            evenCondition = printLock.newCondition();
        }

        public boolean printNextOdd() throws InterruptedException {
            while (!printOdd) {
                await(oddCondition);
            }
            count++;
            boolean toReturn = false;
            if (count <= upperLimit) {
                printOdd();
                toReturn = true;
            }
            printOdd = false;
            signalAll(evenCondition);
            return toReturn;
        }

        public boolean printNextEven() throws InterruptedException {
            while (printOdd) {
                await(evenCondition);
            }
            count++;
            boolean toReturn = false;
            if (count <= upperLimit) {
                printEven();
                toReturn = true;
            }
            printOdd = true;
            signalAll(oddCondition);
            return toReturn;
        }

        private void await(Condition condition) throws InterruptedException {
            printLock.lockInterruptibly();
            try {
                condition.await();
            } finally {
                printLock.unlock();
            }
        }

        private void signalAll(Condition condition) throws InterruptedException {
            printLock.lockInterruptibly();
            try {
                condition.signalAll();
            } finally {
                printLock.unlock();
            }
        }

        public void printOdd() {
            System.out.println("Odd: " + count);
        }

        public void printEven() {
            System.out.println("Even: " + count);
        }

    }

    @Test
    public void evenOdd() throws InterruptedException {
        final Counter counter = new Counter(20);

        Printer oddPrinter = new Printer(new OddPrinter(counter));
        Printer evenPrinter = new Printer(new EvenPrinter(counter));

        Thread oddThread = new Thread(oddPrinter);
        Thread evenThread = new Thread(evenPrinter);

        oddThread.start();
        evenThread.start();

        oddThread.join();
        evenThread.join();
    }

}
