package com.study.week3.mission3.session;

import java.net.Socket;
import java.util.Collection;
import java.util.concurrent.ConcurrentHashMap;

public class SessionManager {
    private final ConcurrentHashMap<String, ChatSession> sessions
            = new ConcurrentHashMap<>();

    public ChatSession createSession(String userId, Socket socket){
        ChatSession session = new ChatSession(userId, socket);
        sessions.put(userId, session);
        return session;
    }

    public ChatSession getSession(String userId) {
        return sessions.get(userId);
    }

    public void removeSession(String userId) {
        sessions.remove(userId);
    }

    public Collection<ChatSession> getAllSession(){
        return sessions.values();
    }

    public int getSessionCount(){
        return sessions.size();
    }
}
