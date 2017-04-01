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

import com.google.common.util.concurrent.ThreadFactoryBuilder;
import com.mwlodarczyk.multithreading.utils.AbstractFuturesTest;

import org.junit.Test;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import java.util.function.Function;

public class S26_Promises extends AbstractFuturesTest {

    private static final ScheduledExecutorService pool =
            Executors.newScheduledThreadPool(10,
                    new ThreadFactoryBuilder()
                            .setDaemon(true)
                            .setNameFormat("FutureOps-%d")
                            .build()
            );

    public static <T> CompletableFuture<T> never() {
        return new CompletableFuture<>();
    }

    public static <T> CompletableFuture<T> timeoutAfter(int millis) {
        final CompletableFuture<T> promise = new CompletableFuture<>();
        pool.schedule(() -> promise.completeExceptionally(new TimeoutException()), millis, TimeUnit.MILLISECONDS);
        return promise;
    }

    @Test
    public void testPromise() throws InterruptedException, ExecutionException, TimeoutException {
        final CompletableFuture<String> future = questions("java");
        final CompletableFuture<String> timeout = timeoutAfter(1);

        future.applyToEither(timeout, str -> {
            System.out.println("Hello");
            return str;
        }).exceptionally(throwable -> {
            System.out.println("Exception");
            return null;
        });

        //Why not using?
        //String str = future.get(2, TimeUnit.SECONDS);
    }
}

