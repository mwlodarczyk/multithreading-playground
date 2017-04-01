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

import com.mwlodarczyk.multithreading.stackoverflow.Question;
import com.mwlodarczyk.multithreading.utils.AbstractFuturesTest;

import org.jsoup.nodes.Document;
import org.junit.Test;

import java.util.concurrent.CompletableFuture;

public class S21_FlatMap extends AbstractFuturesTest {

    @Test
    public void thenApplyIsWrong() throws Exception {
        CompletableFuture<CompletableFuture<Question>> result
                = javaQuestions().thenApply(doc -> findMostInterestingQuestion(doc));
    }

    @Test
    public void thenAcceptIsPoor() throws Exception {
        javaQuestions().thenAccept(document -> {
            findMostInterestingQuestion(document).thenAccept(question -> {
                googleAnswer(question).thenAccept(answer -> {
                    postAnswer(answer).thenAccept(status -> {
                        if (status.intValue() == 200) {
                            System.out.println("OK");
                        } else {
                            System.out.println("Wrong status code: " + status);
                        }
                    });
                });
            });
        });
    }

    @Test
    public void thenCompose() throws Exception {
        final CompletableFuture<Document> java = javaQuestions();

        final CompletableFuture<Question> questionFuture =
                java.thenCompose(doc -> findMostInterestingQuestion(doc));

        final CompletableFuture<String> answerFuture =
                questionFuture.thenCompose(question -> googleAnswer(question));

        final CompletableFuture<Integer> httpStatusFuture =
                answerFuture.thenCompose(answer -> postAnswer(answer));

        httpStatusFuture.thenAccept(status -> {
            if (status.intValue() == 200) {
                System.out.println("OK");
            } else {
                System.out.println("Wrong status code: " + status);
            }
        });
    }

    @Test
    public void chained() throws Exception {
        javaQuestions().
                thenCompose(doc -> findMostInterestingQuestion(doc)).
                thenCompose(question -> googleAnswer(question)).
                thenCompose(answer -> postAnswer(answer)).
                thenAccept(status -> {
                    if (status.intValue() == 200) {
                        System.out.println("OK");
                    } else {
                        System.out.println("Wrong status code: " + status);
                    }
                });
    }

    private CompletableFuture<Document> javaQuestions() {
        return CompletableFuture.supplyAsync(() ->
                        client.mostRecentQuestionsAbout("java"),
                executorService
        );
    }

    private CompletableFuture<Question> findMostInterestingQuestion(Document document) {
        return CompletableFuture.completedFuture(new Question());
    }

    private CompletableFuture<String> googleAnswer(Question q) {
        return CompletableFuture.completedFuture("42");
    }

    private CompletableFuture<Integer> postAnswer(String answer) {
        return CompletableFuture.completedFuture(200);
    }

}
