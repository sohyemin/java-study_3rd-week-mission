package com.study.week3.mission3.session;


import com.study.week3.mission3.model.ChatMessage;

import java.net.Socket;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class ChatSession {
    private final String userId;
    private  final Socket socket;
    private final List<ChatMessage> logs =
            Collections.synchronizedList(new ArrayList<>());

    public ChatSession(String userId, Socket socket) {
        this.userId = userId;
        this.socket = socket;
    }

    public String getUserId() {
        return userId;
    }

    public Socket getSocket() {
        return socket;
    }
}
