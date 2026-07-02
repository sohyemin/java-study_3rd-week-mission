package com.study.week3.mission1;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadFactory;

public class PerformanceTest {

    static CountDownLatch latch = new CountDownLatch(10000);


    private static void platformThreadWithIoBound() throws InterruptedException {
        CountDownLatch latch = new CountDownLatch(10000);

        Runnable task = createTask(latch);

        try (ExecutorService executorService = Executors.newFixedThreadPool(100)) {
            for (int i = 0; i < 10000; i++) {
                executorService.submit(task);
            }

            latch.await();
        }
    }

    private static void virtualThreadWithIoBound() {
        CountDownLatch latch = new CountDownLatch(10000);

        Runnable task = createTask(latch);

        ThreadFactory factory = Thread.ofVirtual().name("myVirtual-", 0).factory();
        try (ExecutorService executorService = Executors.newThreadPerTaskExecutor(factory)) {
            for (int i = 0; i < 10000; i++) {
                executorService.submit(task);
            }

            latch.await();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    private static Runnable createTask(CountDownLatch latch) {
        return () -> {
            try {
                Thread.sleep(2000);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            } finally {
                latch.countDown();
            }
        };
    }

    public static void main(String[] args) throws InterruptedException {

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
