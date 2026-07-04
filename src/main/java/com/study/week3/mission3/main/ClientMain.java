package com.study.week3.mission3.main;

import java.io.*;
import java.net.Socket;
import java.nio.charset.StandardCharsets;

public class ClientMain {
    public static void main(String[] args) {
        try (
                Socket socket = new Socket("localhost", 8080);

                BufferedReader serverReader = new BufferedReader(
                        new InputStreamReader(socket.getInputStream(), StandardCharsets.UTF_8)
                );

                BufferedWriter serverWriter = new BufferedWriter(
                        new OutputStreamWriter(socket.getOutputStream(), StandardCharsets.UTF_8)
                );

                BufferedReader consoleReader = new BufferedReader(
                        new InputStreamReader(System.in, StandardCharsets.UTF_8)
                )
        ) {
            System.out.println("AI Gateway 서버에 연결되었습니다.");
            System.out.println("질문을 입력하세요. 종료하려면 exit 입력.");

            Thread responseReader = Thread.startVirtualThread(() -> {
                try {
                    int ch;
                    while ((ch = serverReader.read()) != -1) {
                        System.out.print((char) ch);
                    }
                } catch (IOException e) {
                    System.out.println("서버 응답 수신 종료: " + e.getMessage());
                }
            });

            while (true) {
                System.out.print("\n> ");
                String message = consoleReader.readLine();

                if (message == null || message.equalsIgnoreCase("exit")) {
                    break;
                }

                serverWriter.write(message);
                serverWriter.newLine();
                serverWriter.flush();
            }

        } catch (IOException e) {
            System.out.println("클라이언트 오류: " + e.getMessage());
        }
    }
}
