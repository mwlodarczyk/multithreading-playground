package com.mwlodarczyk.multithreading;

import org.junit.Test;

import java.util.Collections;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

//Taken from: http://crunchify.com/hashmap-vs-concurrenthashmap-vs-synchronizedmap-how-a-hashmap-can-be-synchronized-in-java/
public class S11_ConcurrentHashMap {

    public final static int THREAD_POOL_SIZE = 5;

    public static Map<String, Integer> hashTableObject = null;
    public static Map<String, Integer> synchronizedMapObject = null;
    public static Map<String, Integer> concurrentHashMapObject = null;

    public static void performTest(final Map<String, Integer> hashMap) throws InterruptedException {

        System.out.println("Test started for: " + hashMap.getClass());
        long averageTime = 0;

        for (int i = 0; i < 5; i++) {

            long startTime = System.nanoTime();
            ExecutorService executorService = Executors.newFixedThreadPool(THREAD_POOL_SIZE);

            for (int j = 0; j < THREAD_POOL_SIZE; j++) {
                executorService.execute(new Runnable() {
                    @SuppressWarnings("unused")
                    @Override
                    public void run() {

                        for (int i = 0; i < 500000; i++) {
                            Integer randomNumber = (int) Math.ceil(Math.random() * 550000);
                            Integer retrievedValue = hashMap.get(String.valueOf(randomNumber));
                            hashMap.put(String.valueOf(randomNumber), randomNumber);
                        }
                    }
                });
            }

            executorService.shutdown();
            executorService.awaitTermination(Long.MAX_VALUE, TimeUnit.DAYS);

            long entTime = System.nanoTime();
            long totalTime = (entTime - startTime) / 1000000L;
            averageTime += totalTime;
            System.out.println("2500K entries added/retrieved in " + totalTime + " ms");
        }
        System.out.println("For " + hashMap.getClass() + " the average time is " + averageTime / 5 + " ms\n");
    }

    @Test
    public void concurrentHashMap() throws InterruptedException {

        hashTableObject = new Hashtable<>();
        performTest(hashTableObject);

        synchronizedMapObject = Collections.synchronizedMap(new HashMap<String, Integer>());
        performTest(synchronizedMapObject);

        concurrentHashMapObject = new ConcurrentHashMap<>();
        performTest(concurrentHashMapObject);
    }


}
