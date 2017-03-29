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
import java.util.concurrent.ExecutionException;

public class S23_AllAny extends AbstractFuturesTest {

	@Test
	public void allOf() throws Exception {
		final CompletableFuture<String> java = questions("java");
		final CompletableFuture<String> scala = questions("scala");
		final CompletableFuture<String> clojure = questions("clojure");
		final CompletableFuture<String> groovy = questions("groovy");

		final CompletableFuture<Void> allCompleted =
				CompletableFuture.allOf(
						java, scala, clojure, groovy
				);

		allCompleted.thenRun(() -> {
			try {
				System.out.println("Loaded: " + java.get());
				System.out.println("Loaded: " + scala.get());
				System.out.println("Loaded: " + clojure.get());
				System.out.println("Loaded: " + groovy.get());
			} catch (InterruptedException | ExecutionException e) {
				System.out.println("Error: " + e.getMessage());
			}
		});
	}

	@Test
	public void anyOf() throws Exception {
		final CompletableFuture<String> java = questions("java");
		final CompletableFuture<String> scala = questions("scala");
		final CompletableFuture<String> clojure = questions("clojure");
		final CompletableFuture<String> groovy = questions("groovy");

		final CompletableFuture<Object> firstCompleted =
				CompletableFuture.anyOf(
						java, scala, clojure, groovy
				);

		firstCompleted.thenAccept((Object result) -> {
			System.out.println("First: " + result);
		});
	}

}

