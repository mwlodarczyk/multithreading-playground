package com.mwlodarczyk.multithreading;

import org.junit.Test;

//Given the difficulty of using wait and notify correctly, you should use the higher-level
//concurrency utilities instead [...] using wait and notify directly is like programming in
//"concurrency assembly language", as compared to the higher-level language provided by
//java.util.concurrent. There is seldom, if ever, reason to use wait and notify in new code
//By Josh Bloch, Effective Java 2nd Edition
public class S05_WaitNotify {

    class Job {

        private boolean isExecuted = false;

        public synchronized void waitForJob() throws InterruptedException {
            System.out.println("Waiting for a job to be finished");

            while(!isExecuted){
                wait();
            }

            System.out.println("Job executed");
        }

        public synchronized void executeJob() {
            isExecuted = true;
            notifyAll();
        }
    }

    private void processJob(final Job job){
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {

                try {
                    Thread.sleep(5000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                job.executeJob();
            }
        });

        thread.start();
    }

    @Test
    public void waitNotify() throws InterruptedException {
        final Job job = new Job();
        processJob(job);
        job.waitForJob();

        assert (job.isExecuted);
    }

}
