/**
 * Tomasz Nurkiewicz
 *
 * @tnurkiewicz | @4financeit
 * >
 * www.nurkiewicz.com
 *
 * https://github.com/nurkiewicz/rx-legacy
 */
package com.mwlodarczyk.multithreading.dao;

import com.mwlodarczyk.multithreading.utils.Sleeper;

import io.reactivex.Observable;

public class PersonDao {

    public Person findById(int id) {
        //SQL, SQL, SQL
        System.out.println("Loading: " + id);
        Sleeper.sleep(1000);
        return new Person();
    }

    public Observable<Person> rxFindById(int id) {
        //return Observable.just(findById(id));
        return Observable.fromCallable(() ->
                findById(id)
        );
    }

}
