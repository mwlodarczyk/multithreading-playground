/**
 * Tomasz Nurkiewicz
 *
 * @tnurkiewicz | @4financeit
 * <p>
 * www.nurkiewicz.com
 * <p>
 * www.github.com/nurkiewicz/completablefuture
 */
package com.mwlodarczyk.multithreading.utils;

import com.google.common.util.concurrent.ThreadFactoryBuilder;
import com.mwlodarczyk.multithreading.stackoverflow.ArtificialSleepWrapper;
import com.mwlodarczyk.multithreading.stackoverflow.FallbackStubClient;
import com.mwlodarczyk.multithreading.stackoverflow.HttpStackOverflowClient;
import com.mwlodarczyk.multithreading.stackoverflow.InjectErrorsWrapper;
import com.mwlodarczyk.multithreading.stackoverflow.LoggingWrapper;
import com.mwlodarczyk.multithreading.stackoverflow.StackOverflowClient;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.rules.TestName;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.TimeUnit;

public class AbstractFuturesTest {

    protected final ExecutorService executorService = Executors.newFixedThreadPool(10, threadFactory("Custom"));

    @Rule
    public TestName testName = new TestName();

    protected ThreadFactory threadFactory(String nameFormat) {
        return new ThreadFactoryBuilder().setNameFormat(nameFormat + "-%d").build();
    }

    protected final StackOverflowClient client = new FallbackStubClient(
            new InjectErrorsWrapper(
                    new LoggingWrapper(
                            new ArtificialSleepWrapper(
                                    new HttpStackOverflowClient()
                            )
                    ), "php"
            )
    );

    @Before
    public void logTestStart() {
        System.out.println("Starting: " + testName.getMethodName());
    }

    @After
    public void stopPool() throws InterruptedException {
        executorService.shutdown();
        executorService.awaitTermination(10, TimeUnit.SECONDS);
    }

    protected CompletableFuture<String> questions(String tag) {
        return CompletableFuture.supplyAsync(() ->
                        client.mostRecentQuestionAbout(tag),
                executorService);
    }

}
