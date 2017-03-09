package com.tomtom.multithreading;

import org.junit.Test;

public class S03_Volatile {

    //private static int globalCounter = 0;
    private volatile static int globalCounter = 0;

    static class ChangeListener extends Thread {

        @Override
        public void run() {
            int localCounter = globalCounter;

            while (localCounter < 5) {
                if (localCounter != globalCounter) {
                    System.out.println("Got Change for counter " + globalCounter);
                    localCounter = globalCounter;
                }
            }

            System.out.println("Change listener finished");
        }
    }

    static class ChangeMaker extends Thread {

        @Override
        public void run() {
            int localCounter = globalCounter;

            while (globalCounter < 5) {

                System.out.println("Incrementing counter to " + (localCounter + 1));
                globalCounter = ++localCounter;

                try {
                    Thread.sleep(500);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }

            System.out.println("Change maker finished");
        }
    }

    @Test
    public void testVolatile() {
        Thread listenerThread = new ChangeListener();
        Thread changeThread = new ChangeMaker();

        listenerThread.start();
        changeThread.start();

        try {
            listenerThread.join();
            changeThread.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

}
