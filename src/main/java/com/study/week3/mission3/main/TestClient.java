package com.study.week3.mission3.main;

import java.io.*;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.time.Instant;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.LongAdder;

public class TestClient {

    private static final String HOST = "localhost";
    private static final int PORT = 8080;

    private static final int CLIENT_COUNT = 40;

    public static void main(String[] args) throws InterruptedException {
        CountDownLatch latch = new CountDownLatch(CLIENT_COUNT);

        AtomicInteger successCount = new AtomicInteger();
        AtomicInteger failCount = new AtomicInteger();

        LongAdder totalResponseTimeMillis = new LongAdder();

        ThreadFactory factory = Thread.ofVirtual()
                .name("load-client-", 0)
                .factory();

        Instant start = Instant.now();

        try (ExecutorService executorService =
                     Executors.newThreadPerTaskExecutor(factory)) {

            for (int i = 0; i < CLIENT_COUNT; i++) {
                int clientId = i;

                executorService.submit(() -> {
                    Instant clientStart = Instant.now();

                    try {
                        runSingleClient(clientId);

                        long elapsed = Duration.between(clientStart, Instant.now()).toMillis();
                        totalResponseTimeMillis.add(elapsed);
                        successCount.incrementAndGet();

                    } catch (Exception e) {
                        failCount.incrementAndGet();
                        System.out.println("client-" + clientId + " failed: " + e.getMessage());
                    } finally {
                        latch.countDown();
                    }
                });
            }

            latch.await();
        }

        long totalElapsed = Duration.between(start, Instant.now()).toMillis();

        System.out.println("\n===== Load Test Result =====");
        System.out.println("Clients: " + CLIENT_COUNT);
        System.out.println("Success: " + successCount.get());
        System.out.println("Fail: " + failCount.get());
        System.out.println("Total elapsed: " + totalElapsed + " ms");

        if (successCount.get() > 0) {
            System.out.println("Average response time: "
                    + totalResponseTimeMillis.sum() / successCount.get()
                    + " ms");
        }
    }

    private static void runSingleClient(int clientId) throws IOException {
        try (
                Socket socket = new Socket(HOST, PORT);

                BufferedReader reader = new BufferedReader(
                        new InputStreamReader(socket.getInputStream(), StandardCharsets.UTF_8)
                );

                BufferedWriter writer = new BufferedWriter(
                        new OutputStreamWriter(socket.getOutputStream(), StandardCharsets.UTF_8)
                )
        ) {
            writer.write("1문장으로 답해줘.");
            writer.newLine();
            writer.flush();

            StringBuilder response = new StringBuilder();
            int ch;

            while ((ch = reader.read()) != -1) {
                response.append((char) ch);

                if (response.toString().contains("[AI 응답 완료]")) {
                    break;
                }
            }
        }
    }
}