package com.study.week3.mission3.scheduler;


import com.study.week3.mission3.model.ChatMessage;

import java.util.concurrent.PriorityBlockingQueue;

public class MessageQueue {

    private PriorityBlockingQueue<ChatMessage> queue
            = new PriorityBlockingQueue<>(100, new PriorityCompare());


    public void submit(ChatMessage message){
        queue.add(message);
    }

    public ChatMessage take() throws InterruptedException {
        ChatMessage request = queue.take();
        System.out.println("작업 시작 : " + request.getRequestId());
        return request;
    }

    public void retry(ChatMessage request){
        request.addRetryCount();

        long delay = calculateBackoff(request.getRetryCount());

        try{
            Thread.sleep(delay);
        } catch (InterruptedException e){
            Thread.currentThread().interrupt();
            return;
        }

        queue.add(request);
    }

    public long calculateBackoff(int retryCount){
        return (long) Math.pow(2, retryCount - 1) *1000;
    }
}
