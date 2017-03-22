package com.mwlodarczyk.multithreading;

import org.junit.Test;

public class S12_DoubleCheckLocking {

    private class Helper {

    }

    class HelperSingleThreadCreator {

        private Helper helper = null;

        public Helper getHelper() {

            if (helper == null) {
                helper = new Helper();
            }

            return helper;
        }
    }

    class HelperMultiThreadCreator {

        private Helper helper = null;

        public synchronized Helper getHelper() {

            if (helper == null) {
                helper = new Helper();
            }

            return helper;
        }
    }

    class HelperNotWorkingDoubleCkechLockingThreadCreator {

        private Helper helper = null;

        public Helper getHelper() {

            if (helper == null) {
                synchronized (this) {
                    if (helper == null) {
                        helper = new Helper();
                    }
                }
            }

            return helper;
        }
    }

    class HelperWorkingDoubleCkechLockingThreadCreator {

        private volatile Helper helper = null;

        public Helper getHelper() {
            if (helper == null) {
                synchronized (this) {
                    if (helper == null) {
                        helper = new Helper();
                    }
                }
            }
            return helper;
        }
    }

    @Test
    public void doubleCheckLocking() throws InterruptedException {

    }

}
