package com.mwlodarczyk.multithreading;

import org.junit.Test;

import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicLong;

public class S08_Atomic {

    class AtomicCounter {

        private AtomicLong count = new AtomicLong(0);

        public void increment() {
            boolean updated = false;

            while (!updated) {
                long prevCount = count.get();
                updated = count.compareAndSet(prevCount, prevCount + 1);
            }
        }

        public long count() {
            return count.get();
        }
    }

    @Test
    public void atomicLong() throws InterruptedException {

        AtomicLong atomicLong = new AtomicLong(123);

        long expectedValue = 123;
        long newValue = 234;

        atomicLong.compareAndSet(expectedValue, newValue);
        atomicLong.getAndIncrement();

        assert (atomicLong.get() == 235);
    }

}
