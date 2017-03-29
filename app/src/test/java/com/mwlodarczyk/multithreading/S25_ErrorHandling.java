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

public class S25_ErrorHandling extends AbstractFuturesTest {

    @Test
    public void exceptionsShortCircuitFuture() throws Exception {
        final CompletableFuture<String> questions = questions("php");

        questions.thenApply(r -> {
            System.out.println("Success!");
            return r;
        });
        questions.get();
    }

    @Test
    public void handleExceptions() throws Exception {
        //given
        final CompletableFuture<String> questions = questions("php");

        //when
        final CompletableFuture<String> recovered = questions
                .handle((result, throwable) -> {
                    if (throwable != null) {
                        return "No PHP today due to: " + throwable;
                    } else {
                        return result.toUpperCase();
                    }
                });

        //then
        System.out.println("Handled: " + recovered.get());
    }

    @Test
    public void shouldHandleExceptionally() throws Exception {
        //given
        final CompletableFuture<String> questions = questions("php");

        //when
        final CompletableFuture<String> recovered = questions
                .exceptionally(throwable -> "Sorry, try again later");

        //then
        System.out.println("Done: " + recovered.get());
    }

}

