package com.demo.lib_generic;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class ThreadProducerDemo {

    private static int value = 1;

    public static void main(String[] args) {
        Lock lock = new ReentrantLock();
        Condition oddCondition = lock.newCondition();
        Condition evenCondition = lock.newCondition();
        ExecutorService service = Executors.newCachedThreadPool();
        service.submit(new OddRunnable(lock, oddCondition, evenCondition));
        service.submit(new EvenRunnable(lock, oddCondition, evenCondition));
    }

    public static class OddRunnable implements Runnable {

        private Lock lock;
        private Condition oddCondition;
        private Condition evenCondition;

        public OddRunnable(Lock lock, Condition oddCondition, Condition evenCondition) {
            this.lock = lock;
            this.oddCondition = oddCondition;
            this.evenCondition = evenCondition;
        }

        @Override
        public void run() {
            while (true) {
                lock.lock();
                try {
                    while (value % 2 == 0) {
                        oddCondition.await();
                    }
                    int code = ((value / 2) + 1) % 26;
                    System.out.print(code);
                    Thread.sleep((long) (Math.random() * 200));
                    value++;
                    evenCondition.signalAll();
                } catch (Exception e) {
                    e.printStackTrace();
                } finally {
                    lock.unlock();
                }
            }
        }
    }

    public static class EvenRunnable implements Runnable {

        private Lock lock;
        private Condition oddCondition;
        private Condition evenCondition;

        public EvenRunnable(Lock lock, Condition oddCondition, Condition evenCondition) {
            this.lock = lock;
            this.oddCondition = oddCondition;
            this.evenCondition = evenCondition;
        }

        @Override
        public void run() {
            while (true) {
                lock.lock();
                try {
                    while (value % 2 != 0) {
                        evenCondition.await();
                    }
                    int code = ((value / 2) % 26 - 1) + 'A';
                    System.out.print((char) code);
                    Thread.sleep((long) (Math.random() * 300));
                    value++;
                    oddCondition.signalAll();
                } catch (Exception e) {
                    e.printStackTrace();
                } finally {
                    lock.unlock();
                }
            }
        }
    }
}
