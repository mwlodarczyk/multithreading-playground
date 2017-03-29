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

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

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

    public static <T> CompletableFuture<T> timeoutAfter(Duration duration) {
        final CompletableFuture<T> promise = new CompletableFuture<>();
        pool.schedule(
                () -> promise.completeExceptionally(new TimeoutException()),
                duration.toMillis(), TimeUnit.MILLISECONDS);
        return promise;
    }

    interface Duration {
        long toMillis();
    }
}

