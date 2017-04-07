/**
 * Tomasz Nurkiewicz
 *
 * @tnurkiewicz | @4financeit
 * >
 * www.nurkiewicz.com
 *
 * https://github.com/nurkiewicz/rx-legacy
 */
package com.mwlodarczyk.multithreading.weather;

import com.mwlodarczyk.multithreading.utils.Sleeper;

import io.reactivex.Observable;

public class WeatherClient {

    public Weather fetch(String city) {
        System.out.println("Loading for: " + city);
        Sleeper.sleep(900);
        //HTTP, HTTP, HTTP
        System.out.println("Done: " + city);
        return new Weather();
    }

    public Observable<Weather> rxFetch(String city) {
        return Observable.fromCallable(() -> fetch(city));
    }


}
