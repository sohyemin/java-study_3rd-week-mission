package com.study.week3.mission3.log;

import java.io.BufferedOutputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;

public class ChatLogWriter {
    private final BufferedOutputStream outputStream;

    public ChatLogWriter(String filePath) {
        try {
            this.outputStream = new BufferedOutputStream(
                    new FileOutputStream(filePath, true)
            );
        } catch (IOException e) {
            throw new RuntimeException("로그 파일 생성 실패: " + e.getMessage(), e);
        }
    }

    public synchronized void write(String message) {
        try {
            String log = message + System.lineSeparator();

            outputStream.write(log.getBytes(StandardCharsets.UTF_8));
            outputStream.flush();

        } catch (IOException e) {
            System.out.println("[ChatLogWriter] 로그 저장 실패: " + e.getMessage());
        }
    }

    public void close() {
        try {
            outputStream.close();
        } catch (IOException e) {
            System.out.println("[ChatLogWriter] 로그 파일 닫기 실패: " + e.getMessage());
        }
    }


}
