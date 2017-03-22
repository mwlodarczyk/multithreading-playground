package com.mwlodarczyk.multithreading;

import org.junit.Test;

public class S14_LiveLockIssue {

    public class Spoon {
        private Diner owner;

        public Spoon(Diner d) {
            owner = d;
        }

        public Diner getOwner() {
            return owner;
        }

        public synchronized void setOwner(Diner d) {
            owner = d;
        }

        public synchronized void use() {
            System.out.printf("%s has eaten!", owner.name);
        }
    }

    public class Diner {

        private String name;
        private boolean isHungry;

        public Diner(String n) {
            name = n;
            isHungry = true;
        }

        public String getName() {
            return name;
        }

        public boolean isHungry() {
            return isHungry;
        }

        public void eatWith(Spoon spoon, Diner spouse) {
            while (isHungry) {
                // Don't have the spoon, so wait patiently for spouse.
                if (spoon.owner != this) {
                    try {
                        Thread.sleep(1);
                    } catch (InterruptedException e) {
                        continue;
                    }
                    continue;
                }

                // If spouse is hungry, insist upon passing the spoon.
                if (spouse.isHungry()) {
                    System.out.printf("%s: You eat first my darling %s!%n", name, spouse.getName());
                    spoon.setOwner(spouse);
                    continue;
                }

                // Spouse wasn't hungry, so finally eat
                spoon.use();
                isHungry = false;
                System.out.printf("%s: I am stuffed, my darling %s!%n", name, spouse.getName());
                spoon.setOwner(spouse);
            }
        }
    }

    @Test
    public void liveLockIssue() throws InterruptedException {

        final Diner husband = new Diner("Bob");
        final Diner wife = new Diner("Alice");

        final Spoon s = new Spoon(husband);

        Thread husbandThread = new Thread(new Runnable() {
            public void run() {
                husband.eatWith(s, wife);
            }
        });

        Thread wifeThread = new Thread(new Runnable() {
            public void run() {
                wife.eatWith(s, husband);
            }
        });

        husbandThread.start();
        wifeThread.start();

        husbandThread.join();
        wifeThread.join();
    }

}
