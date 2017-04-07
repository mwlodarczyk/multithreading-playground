package com.mwlodarczyk.multithreading.cache;

import com.mwlodarczyk.multithreading.utils.Sleeper;

import io.reactivex.Observable;
import io.reactivex.schedulers.Schedulers;

public class CacheServer {

	public String findBy(long key) {
		System.out.println("Loading from memcached: " + key);
		Sleeper.sleep(1000);
		System.out.println("Cache hit for: " + key);
		return "<data>" + key + "</data>";
	}

	public Observable<String> rxFindBy(long key) {
		return Observable
				.fromCallable(() -> findBy(key))
				.subscribeOn(Schedulers.io());
	}

}
