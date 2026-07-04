package com.study.week3.mission3.server;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.util.concurrent.BlockingQueue;

public class ClientStreamWriter implements Runnable{

    private final BufferedWriter writer;
    private final BlockingQueue<String> queue;

    public ClientStreamWriter(BufferedWriter writer, BlockingQueue<String> queue) {
        this.writer = writer;
        this.queue = queue;
    }

    @Override
    public void run() {
        try {
            while (true) {
                String chunk = queue.take();

                if ("[END]".equals(chunk)){
                    break;
                }

                writer.write(chunk);
                writer.flush();
            }

            writer.write("\n[AI 응답 완료]\n");
            writer.flush();

        } catch (InterruptedException e){
            Thread.currentThread().interrupt();
        } catch (IOException e) {
            System.out.println("클라이언트 전송 실패 : " + e.getMessage());
        }
    }
}
