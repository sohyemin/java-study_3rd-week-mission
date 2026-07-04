package com.study.week3.mission3.server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class SocketServer {

    private static final int PORT = 8080;

    public void start() {

        try (
            ServerSocket serverSocket = new ServerSocket(PORT);
            ExecutorService executorService =
                    Executors.newThreadPerTaskExecutor(
                            Thread.ofVirtual()
                                    .name("client-handler-", 0)
                                    .factory()
                    );
        ){
            System.out.println("서버 시작, [port]: " + PORT);

            while (true){
                Socket socket = serverSocket.accept();
                System.out.println("클라이언트 연결, [port]: " + PORT);
                executorService.submit(new ClientHandler(socket));
            }
        } catch (Exception e) {
            System.out.println("accept 실패 : "+e.getMessage());
        }
    }

}