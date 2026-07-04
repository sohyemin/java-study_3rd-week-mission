package com.study.week3.mission3.engine;

import java.util.function.Consumer;

public interface StreamingAIEngine {
    void streamChat(String message, Consumer<String> chunkoConsumer);
}
