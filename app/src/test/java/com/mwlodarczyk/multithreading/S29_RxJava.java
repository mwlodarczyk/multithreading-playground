/**
 * Tomasz Nurkiewicz
 *
 * @tnurkiewicz | @4financeit
 *
 * www.nurkiewicz.com
 *
 * https://github.com/nurkiewicz/rx-legacy
 */
package com.mwlodarczyk.multithreading;

import com.mwlodarczyk.multithreading.cache.CacheServer;
import com.mwlodarczyk.multithreading.dao.Person;
import com.mwlodarczyk.multithreading.dao.PersonDao;
import com.mwlodarczyk.multithreading.weather.Weather;
import com.mwlodarczyk.multithreading.weather.WeatherClient;

import org.junit.Test;

import java.io.File;
import java.math.BigDecimal;
import java.util.List;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.observers.TestObserver;
import io.reactivex.schedulers.Schedulers;
import io.reactivex.schedulers.TestScheduler;
import io.reactivex.subscribers.TestSubscriber;

import static java.util.concurrent.TimeUnit.MILLISECONDS;

public class S29_RxJava {

    public static final BigDecimal FALLBACK = BigDecimal.ONE.negate();

    private final WeatherClient weatherClient = new WeatherClient();
    private final PersonDao personDao = new PersonDao();

    void print(Object obj) {
        System.out.println("Got: " + obj);
    }

    @Test
    public void sample_1() throws Exception {
        final Observable<String> observable = Observable.just("1");
        observable.subscribe(this::print);
    }

    @Test
    public void sample_2() throws Exception {
        final Observable<String> observable = Observable.just("1", "2", "3");
        observable.subscribe(this::print);
    }

    @Test
    public void sample_3() throws Exception {
        print(weatherClient.fetch("Warsaw"));
    }

    @Test
    public void sample_4() throws Exception {
        final Observable<Weather> warsaw = weatherClient.rxFetch("Warsaw");
        //warsaw.subscribe(w -> this.print(w));
    }

    @Test
    public void sample_5() throws Exception {
        final Observable<Weather> warsaw = weatherClient.rxFetch("Warsaw");
        final Observable<Weather> withTimeout = warsaw.timeout(1, TimeUnit.SECONDS);
        withTimeout.subscribe(this::print);
    }

    @Test
    public void sample_6() throws Exception {
        final Observable<Weather> warsaw = weatherClient.rxFetch("Warsaw");
        final Observable<Weather> withTimeout = warsaw.timeout(800, MILLISECONDS);
        withTimeout.subscribe(this::print);
    }

    @Test
    public void sample_7() throws Exception {
        Observable<Weather> weather1 = weatherClient.rxFetch("Warsaw");
        Observable<Weather> weather2 = weatherClient.rxFetch("Lodz");
        final Observable<Weather> mergedWeather = weather1.mergeWith(weather2);
        mergedWeather.subscribe(this::print);
    }

    @Test
    public void sample_8() throws Exception {
        final Observable<Weather> weather = weatherClient
                .rxFetch("Warsaw")
                .subscribeOn(Schedulers.io()); //Do not use io

        final Observable<Person> person = personDao
                .rxFindById(42)
                .subscribeOn(Schedulers.io()); //Do not use io

        final Observable<String> zipped = weather.zipWith(person, (Weather w, Person p) -> w + " : " + p);
        zipped.subscribe(this::print);

        print("Waiting for http");
        Thread.sleep(2000);
    }

    @Test
    public void sample_9() throws Exception {
        final Observable<String> strings = Observable.just("A", "B", "C");
        final Observable<Integer> numbers = Observable
                .range(1, 3)
                .map(x -> x * 10);

        final Observable<String> zipped = Observable.zip(
                strings,
                numbers,
                (s, n) -> s + n
        );

        zipped.subscribe(this::print);
    }

    @Test
    public void sample_10() throws Exception {
        //Schedulers.io(); //number of operations
        //Schedulers.newThread();
        //Schedulers.computation(); //number of cores
        //Schedulers.from(Executors.newFixedThreadPool(10));
        new ThreadPoolExecutor(10, 10,
                0L, MILLISECONDS, new LinkedBlockingQueue<>());
    }

    @Test
    public void sample_11() throws Exception {

        CacheServer eu = new CacheServer();
        CacheServer us = new CacheServer();

        Observable<String> reu = eu.rxFindBy(42);
        Observable<String> rus = us.rxFindBy(42);

        Observable
                .merge(reu, rus)
                .first("")
                .subscribe(this::print);

        TimeUnit.SECONDS.sleep(2);
    }

    @Test
    public void sample_12() throws Exception {
        Observable
                .interval(1, TimeUnit.SECONDS)
                .map(x -> x * Math.PI)
                .subscribe(this::print);
        TimeUnit.SECONDS.sleep(5);
    }

    File dir = new File("/Users/wlodarczyk/Desktop/TestFiles");

    private Observable<String> childrenOf(File dir) {
        final File[] files = dir.listFiles();
        return Observable
                .fromArray(files)
                .map(File::getName);
    }

    @Test
    public void sample_13() throws Exception {
        childrenOf(dir)
                .subscribe(this::print);

        Thread.sleep(100);
    }

    List<String> childrenOf2(File dir) {
        return childrenOf(dir)
                .toList()
                .blockingGet();
    }

    @Test
    public void sample_14() throws Exception {
        Observable
                .interval(1, TimeUnit.SECONDS)
                .map(x -> childrenOf2(dir))
                .subscribe(this::print);

        Thread.sleep(10000);
    }

    @Test
    public void sample_15() throws Exception {
        Observable
                .interval(1, TimeUnit.SECONDS)
                .flatMap(x -> childrenOf(dir))
                .distinct()  //memory leak
                .subscribe(this::print);

        Thread.sleep(10000);
    }

    @Test
    public void sample_16() throws Exception {
        final TestScheduler testScheduler = new TestScheduler();

        final Observable<BigDecimal> response = verySlowSoapService()
                .timeout(1, TimeUnit.SECONDS, testScheduler)
                .doOnError(ex -> System.out.println("Ops: " + ex)) //never do that
                .retry(4)
                .onErrorReturn(x -> BigDecimal.ONE.negate());


        //response.blockingNext().forEach(this::print);//.subscribe(this::print);

        final TestObserver<BigDecimal> subscriber = new TestObserver<>();
        response.subscribe(subscriber);

        subscriber.assertNoErrors();
        subscriber.assertNoValues();

        testScheduler.advanceTimeBy(4_999, MILLISECONDS);
        subscriber.assertNoErrors();
        subscriber.assertNoValues();

        testScheduler.advanceTimeBy(1, MILLISECONDS);
        subscriber.assertNoErrors();
        subscriber.assertValue(BigDecimal.ONE.negate());
    }

    private Observable<BigDecimal> verySlowSoapService() {
        return Observable
                .timer(1, TimeUnit.MINUTES)
                .map(x -> BigDecimal.ZERO);
    }

}

