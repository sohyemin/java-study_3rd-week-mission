package com.study.week3.mission3.server;

import com.study.week3.mission3.session.ChatSession;
import com.study.week3.mission3.session.SessionManager;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;

public class BroadcastManager {
    private final SessionManager manager;

    public BroadcastManager(SessionManager manager) {
        this.manager = manager;
    }

    public void broadcast(String message){
        for(ChatSession session : manager.getAllSession()){
            send(session, message);
        }
    }

    private void send(ChatSession session, String message) {
        Socket socket = session.getSocket();

        try {
            PrintWriter writer = new PrintWriter(socket.getOutputStream(), true);
            writer.println("[AI] " + message);
        } catch (IOException e) {
            System.out.println("[BroadcastManager] 전송 실패 userId="
                    + session.getUserId()
                    + ", reason="
                    + e.getMessage());

            manager.removeSession(session.getUserId());
        }
    }
}
