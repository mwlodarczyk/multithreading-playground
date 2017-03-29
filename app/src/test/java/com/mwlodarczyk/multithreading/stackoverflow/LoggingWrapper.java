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

import com.google.common.base.Joiner;
import com.google.common.base.Splitter;
import com.google.common.collect.Iterables;

import org.jsoup.nodes.Document;

public class LoggingWrapper implements StackOverflowClient {

    private final StackOverflowClient target;

    public LoggingWrapper(StackOverflowClient target) {
        this.target = target;
    }

    @Override
    public String mostRecentQuestionAbout(String tag) {
        System.out.println("Entering mostRecentQuestionAbout: " + tag);
        final String title = target.mostRecentQuestionAbout(tag);
        System.out.println("Leaving mostRecentQuestionAbout: " + tag + ", " + title);
        return title;
    }

    @Override
    public Document mostRecentQuestionsAbout(String tag) {
        System.out.println("Entering mostRecentQuestionAbout: " + tag);
        final Document document = target.mostRecentQuestionsAbout(tag);
        System.out.println("Leaving mostRecentQuestionsAbout: " + tag + ", " + htmlExcerpt(document));
        return document;
    }

    private String htmlExcerpt(Document document) {
        final String outerHtml = document.outerHtml();
        final Iterable<String> lines = Splitter.onPattern("\r?\n").split(outerHtml);
        final Iterable<String> firstLines = Iterables.limit(lines, 4);
        final String excerpt = Joiner.on(' ').join(firstLines);
        final int remainingBytes = Math.max(outerHtml.length() - excerpt.length(), 0);
        return excerpt + " [...and " + remainingBytes + " chars]";
    }
}
