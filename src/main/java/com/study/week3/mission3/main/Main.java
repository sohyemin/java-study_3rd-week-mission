package com.study.week3.mission3.main;

import com.study.week3.mission3.server.SocketServer;

public class Main {
    public static void main(String[] args) throws Exception{

        SocketServer server = new SocketServer();

        server.start();
    }
}