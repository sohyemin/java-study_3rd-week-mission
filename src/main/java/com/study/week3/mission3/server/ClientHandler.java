package com.study.week3.mission3.server;

import com.study.week3.mission3.engine.OllamaStreamingEngine;
import com.study.week3.mission3.engine.StreamingAIEngine;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.nio.charset.StandardCharsets;

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
                System.out.println("사용자 요청 : " + message);

                writer.write("[AI 응답 시작]\n");
                writer.flush();

                aiEngine.streamChat(message, chunk -> {
                    try{
                        writer.write(chunk);
                        writer.flush();
                    } catch (Exception e) {
                        throw new RuntimeException(e);
                    }
                });

                writer.write("[AI 응답 완료]\n");
                writer.flush();
            }
        } catch (Exception e) {
            System.out.println("클라이언트 연결 종료: " + e.getMessage());
        }
    }

}
