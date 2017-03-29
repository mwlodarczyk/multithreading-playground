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

public class S22_Zip extends AbstractFuturesTest {

    @Test
    public void thenCombine() throws Exception {
        final CompletableFuture<String> java = questions("java");
        final CompletableFuture<String> scala = questions("scala");

        final CompletableFuture<Integer> both = java.
                thenCombine(scala, (String javaTitle, String scalaTitle) ->
                        javaTitle.length() + scalaTitle.length()
                );

        both.thenAccept(length -> System.out.println("Total length: " + length));
    }

    @Test
    public void either() throws Exception {
        final CompletableFuture<String> java = questions("java");
        final CompletableFuture<String> scala = questions("scala");

        final CompletableFuture<String> both = java.
                applyToEither(scala, title -> title.toUpperCase());

        both.thenAccept(title -> System.out.println("First: " + title));
    }


}

