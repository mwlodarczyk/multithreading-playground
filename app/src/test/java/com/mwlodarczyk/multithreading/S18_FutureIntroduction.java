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

import com.mwlodarczyk.multithreading.stackoverflow.LoadFromStackOverflowTask;
import com.mwlodarczyk.multithreading.utils.AbstractFuturesTest;

import org.junit.Test;

import java.util.concurrent.Callable;
import java.util.concurrent.Future;

public class S18_FutureIntroduction extends AbstractFuturesTest {

    /**
     * Broken abstraction - blocking method calls
     */
    @Test
    public void blockingCall() throws Exception {
        final String title = client.mostRecentQuestionAbout("java");
        System.out.println("Most recent Java question: :" + title);
    }

    @Test
    public void executorService() throws Exception {
        final Callable<String> task = () -> client.mostRecentQuestionAbout("java");
        final Future<String> javaQuestionFuture = executorService.submit(task);
        //...
        final String javaQuestion = javaQuestionFuture.get();
        System.out.println("Found: " + javaQuestion);
    }

    /**
     * Composing is impossible
     */
    @Test
    public void waitForFirstOrAll() throws Exception {
        final Future<String> java = findQuestionsAbout("java");
        final Future<String> scala = findQuestionsAbout("scala");

        //???
    }

    private Future<String> findQuestionsAbout(String tag) {
        final Callable<String> task = new LoadFromStackOverflowTask(client, tag);
        return executorService.submit(task);
    }

}

