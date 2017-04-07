/**
 * Tomasz Nurkiewicz
 *
 * @tnurkiewicz | @4financeit
 * >
 * www.nurkiewicz.com
 *
 * https://github.com/nurkiewicz/rx-legacy
 */
package com.mwlodarczyk.multithreading.utils;

import com.google.common.base.Throwables;

public class Sleeper {

    public static void sleep(int millis) {
        try {
            Thread.sleep(millis);
        } catch (InterruptedException e) {
            throw Throwables.propagate(e);
        }
    }

}
