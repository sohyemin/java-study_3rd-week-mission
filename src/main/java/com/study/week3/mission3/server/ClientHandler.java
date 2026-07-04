package com.study.week3.mission3.server;

import com.study.week3.mission3.model.ChatMessage;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.nio.charset.StandardCharsets;

public class ClientHandler implements Runnable{
    private final Socket socket;

    public ClientHandler(Socket socket) {
        this.socket = socket;
    }

    @Override
    public void run() {
        handle();
    }

    private void handle() {
        try(
                socket;
                BufferedReader reader = new BufferedReader(
                        new InputStreamReader(socket.getInputStream(),
                                StandardCharsets.UTF_8)
                );
                BufferedWriter writer = new BufferedWriter(
                        new OutputStreamWriter(socket.getOutputStream(),
                                StandardCharsets.UTF_8)
                );

        ){
            writer.write("AI Gateway에 연결되었습니다\n");
            writer.flush();

            String message;

            while((message = reader.readLine()) != null){
                System.out.println("사용자 요청 : " + message);

                writer.write("[AI 응답 시작]\n");
                writer.flush();

                String[] chunks = {"안", "녕", "하", "세", "요"};

                for (String chunk : chunks) {
                    writer.write(chunk);
                    writer.flush();

                    Thread.sleep(200);
                }

                writer.write("[AI 응답 완료]\n");
                writer.flush();
            }
        } catch (Exception e){
            System.out.println("클라이언트 연결 종료: " + e.getMessage());
        }
    }

}
