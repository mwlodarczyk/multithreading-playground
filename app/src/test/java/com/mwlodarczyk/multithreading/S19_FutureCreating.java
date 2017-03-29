/**
 * Tomasz Nurkiewicz
 *
 * @tnurkiewicz | @4financeit
 * <p>
 * www.nurkiewicz.com
 * <p>
 * www.github.com/nurkiewicz/completablefuture
 */
package com.mwlodarczyk.multithreading;

import com.mwlodarczyk.multithreading.utils.AbstractFuturesTest;

import org.junit.Test;

import java.util.concurrent.CompletableFuture;

public class S19_FutureCreating extends AbstractFuturesTest {

    /**
     * Already completed future
     */
    @Test
    public void completed() throws Exception {
        final CompletableFuture<Integer> answer =
                CompletableFuture.completedFuture(42);

        final int fortyTwo = answer.get();  //does not block
    }

    /**
     * Built-in thread pool
     */
    @Test
    public void supplyAsync() throws Exception {
        final CompletableFuture<String> java =
                CompletableFuture.supplyAsync(() ->
                        client.mostRecentQuestionAbout("java")
                );
        System.out.println("Found: " + java.get());
    }

    /**
     * Custom thread pool, equivalent (*) to submit()
     */
    @Test
    public void supplyAsyncWithCustomExecutor() throws Exception {
        final CompletableFuture<String> java =
                CompletableFuture.supplyAsync(
                        () -> client.mostRecentQuestionAbout("java"),
                        executorService
                );
        System.out.println("Found: " + java.get());
    }

}

