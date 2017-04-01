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
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class S24_AsyncCallback extends AbstractFuturesTest {

    protected final ExecutorService poolAlpha =
            Executors.newFixedThreadPool(10, threadFactory("Alpha"));
    protected final ExecutorService poolBeta =
            Executors.newFixedThreadPool(10, threadFactory("Beta"));
    protected final ExecutorService poolGamma =
            Executors.newFixedThreadPool(10, threadFactory("Gamma"));

    @Test
    public void whichThreadInvokesCallbacks() throws Exception {
        final CompletableFuture<String> java = CompletableFuture
                .supplyAsync(
                        () -> client.mostRecentQuestionAbout("java"),
                        poolAlpha);
        final CompletableFuture<String> scala = CompletableFuture
                .supplyAsync(
                        () -> client.mostRecentQuestionAbout("scala"),
                        poolBeta);

        final CompletableFuture<String> first = java
                .applyToEither(scala, question -> {
                    System.out.println("First: " + question);
                    return question.toUpperCase();
                });

        first.thenAccept(q -> System.out.println("Sync: " + q));
        first.thenAcceptAsync(q -> System.out.println("Async: " + q));
        first.thenAcceptAsync(q -> System.out.println("Async (pool): " + q), poolGamma);
        first.get();        //block
    }

}

