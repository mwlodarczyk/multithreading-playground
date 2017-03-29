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

import org.jsoup.nodes.Document;

public class FallbackStubClient implements StackOverflowClient {

    private final StackOverflowClient target;

    public FallbackStubClient(StackOverflowClient target) {
        this.target = target;
    }

    @Override
    public String mostRecentQuestionAbout(String tag) {
        try {
            return target.mostRecentQuestionAbout(tag);
        } catch (Exception e) {
            System.out.println("Problem retrieving tag: " + tag + " due to " + e.getMessage());
            switch (tag) {
                case "java":
                    return "How to generate xml report with maven depencency?";
                case "scala":
                    return "Update a timestamp SettingKey in an sbt 0.12 task";
                case "groovy":
                    return "Reusing Grails variables inside Config.groovy";
                case "clojure":
                    return "Merge two comma delimited strings in Clojure";
                default:
                    throw e;
            }
        }
    }

    @Override
    public Document mostRecentQuestionsAbout(String tag) {
        try {
            return target.mostRecentQuestionsAbout(tag);
        } catch (Exception e) {
            System.out.println("Problem retrieving tag: " + tag + " due to " + e.getMessage());
            return loadStubHtmlFromDisk(tag);
        }
    }

    private Document loadStubHtmlFromDisk(String tag) {
        throw new RuntimeException("");
        /*try {
            final URL resource = getClass().getResource("/" + tag + "-questions.html");
            final String html = IOUtils.toString(resource);
            return Jsoup.parse(html);
        } catch (IOException e) {
            throw Throwables.propagate(e);
        }*/
    }
}
