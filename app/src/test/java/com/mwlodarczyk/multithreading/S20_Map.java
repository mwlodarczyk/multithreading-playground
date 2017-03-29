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

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.junit.Test;

import java.util.concurrent.CompletableFuture;

public class S20_Map extends AbstractFuturesTest {

    @Test
    public void oldSchool() throws Exception {
        final CompletableFuture<Document> java =
                CompletableFuture.supplyAsync(() ->
                                client.mostRecentQuestionsAbout("java"),
                        executorService
                );

        final Document document = java.get();       //blocks
        final Element element = document.select("a.question-hyperlink").get(0);
        final String title = element.text();
        final int length = title.length();
        System.out.println("Length: " + length);
    }

    /**
     * Callback hell, doesn't compose
     */
    @Test
    public void callbacksCallbacksEverywhere() throws Exception {
        final CompletableFuture<Document> java =
                CompletableFuture.supplyAsync(() ->
                                client.mostRecentQuestionsAbout("java"),
                        executorService
                );

        java.thenAccept(document -> System.out.println("Downloaded: " + document));
    }

    @Test
    public void thenApply() throws Exception {
        final CompletableFuture<Document> java =
                CompletableFuture.supplyAsync(() ->
                                client.mostRecentQuestionsAbout("java"),
                        executorService
                );

        final CompletableFuture<Element> titleElement =
                java.thenApply((Document doc) ->
                        doc.select("a.question-hyperlink").get(0));

        final CompletableFuture<String> titleText =
                titleElement.thenApply(Element::text);

        final CompletableFuture<Integer> length =
                titleText.thenApply(String::length);

        System.out.println("Length: " + length.get());
    }

    @Test
    public void thenApplyChained() throws Exception {
        final CompletableFuture<Document> java =
                CompletableFuture.supplyAsync(() ->
                                client.mostRecentQuestionsAbout("java"),
                        executorService
                );

        final CompletableFuture<Integer> length = java.
                thenApply(doc -> doc.select("a.question-hyperlink").get(0)).
                thenApply(Element::text).
                thenApply(String::length);


        System.out.println("Length: " + length.get());
    }

}

