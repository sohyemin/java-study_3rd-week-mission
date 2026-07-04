package com.study.week3.mission3.server;

import com.study.week3.mission3.scheduler.MessageQueue;
import com.study.week3.mission3.session.SessionManager;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class SocketServer {

    public void start() throws IOException {

        ServerSocket serverSocket = new ServerSocket(8080);
        System.out.println("서버 시작!");

        while (true) {
            Socket socket = serverSocket.accept();
            System.out.println("클라이언트 연결!");

            Thread.startVirtualThread(
                    new ClientHandler(socket)
            );
        }
    }

}