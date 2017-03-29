/**
 * Tomasz Nurkiewicz
 *
 * @tnurkiewicz | @4financeit
 * <p>
 * www.nurkiewicz.com
 * <p>
 * www.github.com/nurkiewicz/completablefuture
 */
package com.mwlodarczyk.multithreading.stackoverflow;

import java.util.concurrent.Callable;

public class LoadFromStackOverflowTask implements Callable<String> {

    private final StackOverflowClient client;
    private final String tag;

    public LoadFromStackOverflowTask(StackOverflowClient client, String tag) {
        this.client = client;
        this.tag = tag;
    }

    @Override
    public String call() throws Exception {
        return client.mostRecentQuestionAbout(tag);
    }
}
