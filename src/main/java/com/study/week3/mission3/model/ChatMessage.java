package com.study.week3.mission3.model;

import java.time.LocalDateTime;

public class ChatMessage {
    private long requestId;
    private String userId;
    private String prompt;

    private int priority; // 우선도

    private int retryCount; // 재요청 횟수

    private LocalDateTime createdAt;

    public ChatMessage(long requestId, String userId, String prompt, int priority) {
        this.requestId = requestId;
        this.userId = userId;
        this.prompt = prompt;
        this.priority = priority;

        retryCount = 0;
        createdAt = LocalDateTime.now();
    }

    public ChatMessage(String userId, String message) {
    }

    public long getRequestId() {
        return requestId;
    }

    public int getPriority() {
        return priority;
    }

    public int getRetryCount() {
        return retryCount;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void addRetryCount() {
        this.retryCount++;
    }

    public String getPrompt() {
        return prompt;
    }
}
