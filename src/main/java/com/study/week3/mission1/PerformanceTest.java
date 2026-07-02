package com.study.week3.mission1;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadFactory;

public class PerformanceTest {
    private static final Runnable ioBoundRunnable = new Runnable() {
        @Override
        public void run() {
            System.out.println("1) run. thread: " + Thread.currentThread());
            try {
                Thread.sleep(5000);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            System.out.println("2) run. thread: " + Thread.currentThread());
        }

        CountDownLatch latch = new CountDownLatch(10000);

        Runnable task = () -> {
            try {
                Thread.sleep(2000);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            } finally {
                latch.countDown();
            }
        };
    };

    private static void platformThreadWithIoBound() {
        try (ExecutorService executorService = Executors.newFixedThreadPool(10000)) {
            for (int i = 0; i < 10000; i++) {
                executorService.submit(ioBoundRunnable);
            }
        }
    }

    private static void virtualThreadWithIoBound() {
        ThreadFactory factory = Thread.ofVirtual().name("myVirtual-", 0).factory();
        try (ExecutorService executorService = Executors.newThreadPerTaskExecutor(factory)) {
            for (int i = 0; i < 10000; i++) {
                executorService.submit(ioBoundRunnable);
            }
        }
    }

    public static void main(String[] args) {

        System.out.println("platformThread start...");
        System.out.println("==========================");

        long start1 =System.nanoTime();

        platformThreadWithIoBound();

        long end1 =System.nanoTime();

        System.out.println("virtualThread start...");
        System.out.println("==========================");

        long start2 =System.nanoTime();

        virtualThreadWithIoBound();

        long end2 =System.nanoTime();


        System.out.println("platform Thread : " + (end1 - start1) / 1_000_000 + " ms");
        System.out.println("virtual Thread : " + (end2 - start2) / 1_000_000 + " ms");

    }
}
