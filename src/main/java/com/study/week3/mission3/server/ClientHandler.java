package com.study.week3.mission3.server;

import com.study.week3.mission3.engine.OllamaStreamingEngine;
import com.study.week3.mission3.engine.StreamingAIEngine;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

public class ClientHandler implements Runnable {
    private final Socket socket;
    private final StreamingAIEngine aiEngine = new OllamaStreamingEngine();

    public ClientHandler(Socket socket) {
        this.socket = socket;
    }

    @Override
    public void run() {
        handle();
    }

    private void handle() {
        try (
                socket;
                BufferedReader reader = new BufferedReader(
                        new InputStreamReader(socket.getInputStream(),
                                StandardCharsets.UTF_8)
                );
                BufferedWriter writer = new BufferedWriter(
                        new OutputStreamWriter(socket.getOutputStream(),
                                StandardCharsets.UTF_8)
                );

        ) {
            writer.write("AI Gateway에 연결되었습니다\n");
            writer.flush();

            String message;

            while ((message = reader.readLine()) != null) {

                handleMessage(message, writer);

                writer.write("\n[AI 응답 완료]\n");
                writer.flush();
            }
        } catch (Exception e) {
            System.out.println("클라이언트 연결 종료: " + e.getMessage());
        }
    }

    private void handleMessage(String message, BufferedWriter writer) throws Exception {
        System.out.println("사용자 요청 : " + message);

        writer.write("[AI 응답 시작]\n");
        writer.flush();

        BlockingQueue<String> queue = new ArrayBlockingQueue<>(100);

        Thread writerThread = Thread.startVirtualThread(
                new ClientStreamWriter(writer, queue)
        );

        aiEngine.streamChat(message, chunk -> {
            try {
                queue.put(chunk);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                throw new RuntimeException(e);
            }
        });

        queue.put("[END]");
        writerThread.join();
    }

}
