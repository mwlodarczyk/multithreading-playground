/**
 *
 * Tomasz Nurkiewicz
 *
 * @tnurkiewicz | @4financeit
 *
 * www.nurkiewicz.com
 *
 * www.github.com/nurkiewicz/completablefuture
 *
 */

package com.mwlodarczyk.multithreading.stackoverflow;

import org.jsoup.nodes.Document;

public interface StackOverflowClient {

    String mostRecentQuestionAbout(String tag);
    Document mostRecentQuestionsAbout(String tag);

}
