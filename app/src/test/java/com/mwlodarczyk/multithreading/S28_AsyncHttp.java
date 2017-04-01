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

import java.io.IOException;
import java.util.concurrent.CompletableFuture;
import java.util.function.Consumer;

public class S28_AsyncHttp extends AbstractFuturesTest {

    private final AsyncHttpClient asyncHttpClient = new AsyncHttpClient();

    @Test
    public void asyncHttpWithCallbacks() throws Exception {
        CompletableFuture<String> java = loadTag("java");
        java.thenAccept(s -> System.out.println("Response: " + s));
        java.exceptionally(throwable -> {
            System.out.println("Mayday: " + throwable.getMessage());
            return null;
        });

        loadTag(
                "java",
                response -> System.out.println("Response: " + response),
                throwable -> System.out.println("Mayday: " + throwable.getMessage())
        );
    }

    public void loadTag(String tag, Consumer<String> onSuccess, Consumer<Throwable> onError) throws IOException {
        asyncHttpClient
                .prepareGet("http://stackoverflow.com/questions/tagged/" + tag)
                .execute(
                        new AsyncCompletionHandler() {
                            @Override
                            public void onCompleted(Response response) {
                                onSuccess.accept(response.getResponseBody());
                            }

                            @Override
                            public void onThrowable(Throwable t) {
                                onError.accept(t);
                            }
                        }
                );
    }

    public CompletableFuture<String> loadTag(String tag) throws IOException {
        final CompletableFuture<String> promise = new CompletableFuture<>();
        asyncHttpClient.prepareGet("http://stackoverflow.com/questions/tagged/" + tag).execute(
                new AsyncCompletionHandler() {
                    @Override
                    public void onCompleted(Response response) {
                        promise.complete(response.getResponseBody());
                    }

                    @Override
                    public void onThrowable(Throwable t) {
                        promise.completeExceptionally(t);
                    }
                }
        );
        return promise;
    }

    public interface Response {
        String getResponseBody();
    }

    public interface AsyncCompletionHandler {
        void onCompleted(Response response);

        void onThrowable(Throwable t);
    }

    public class AsyncHttpClient {

        AsyncHttpClient prepareGet(String string) {
            return this;
        }

        void execute(AsyncCompletionHandler handler) {
            handler.onCompleted(() -> "sample response");
        }

    }

}

