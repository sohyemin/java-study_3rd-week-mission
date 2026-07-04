# Mission 3 - Pure Java 기반 Virtual Thread Streaming AI Gateway Server

> Java의 `HttpClient`, `Virtual Thread`, `Socket`만을 이용하여 AI Streaming Gateway Server를 구현하고, Streaming 응답 처리와 Backpressure 구조를 학습한다.

---

# 프로젝트 목표

기존 AI API 호출 방식은 응답이 모두 생성될 때까지 기다린 후 사용자에게 한 번에 전달한다.

이번 미션에서는 Spring AI와 같은 라이브러리를 사용하지 않고, **Pure Java**만으로 AI Streaming Gateway를 구현하여 생성되는 응답을 실시간으로 사용자에게 전달하는 구조를 구현하였다.

또한 다수의 사용자가 동시에 AI를 사용할 수 있도록 Virtual Thread를 적용하고, 느린 클라이언트로 인한 메모리 증가를 방지하기 위해 Backpressure를 고려한 구조를 설계하였다.

---

# 학습 목표

- Java HttpClient Streaming 사용
- Virtual Thread 기반 Socket 서버 구현
- Streaming Response 처리
- Consumer Callback 구조 이해
- Producer-Consumer Pattern 이해
- BlockingQueue를 이용한 Backpressure 구현

---

# 시스템 구조

```
                Client

                   │

              Socket Server

                   │

          Virtual Thread(ClientHandler)

                   │

      OllamaStreamingEngine(HttpClient)

                   │

          Ollama Streaming API

                   │

            Streaming Response

                   │

          BlockingQueue(Buffer)

                   │

          ClientStreamWriter

                   │

                Client
```

---

# 구현 내용

## SocketServer

ServerSocket을 이용하여 클라이언트의 연결을 수신한다.

```java
serverSocket.accept();
```

클라이언트가 접속하면 새로운 Socket이 생성되며,

```java
Thread.startVirtualThread(...)
```

를 이용하여 ClientHandler를 Virtual Thread에서 실행한다.

---

## ClientHandler

ClientHandler는 하나의 클라이언트 요청을 처리하는 클래스이다.

역할

- 사용자 질문 수신
- Streaming AI 호출
- Chunk Queue 생성
- ClientStreamWriter 실행
- 응답 종료 처리

흐름

```
Client

↓

readLine()

↓

streamChat()

↓

Queue

↓

Writer
```

---

## StreamingAIEngine

Streaming AI Engine의 인터페이스이다.

```java
void streamChat(
        String message,
        Consumer<String> chunkConsumer
);
```

Streaming 방식에서는 응답 전체를 반환하는 것이 아니라,

생성되는 Chunk마다 Consumer를 호출하도록 설계하였다.

---

## OllamaStreamingEngine

Java HttpClient를 이용하여 Ollama API를 호출한다.

```java
"stream": true
```

옵션을 사용하여 Streaming Response를 활성화하였다.

기존 방식

```
Request

↓

Wait

↓

Full Response

↓

Client
```

Streaming 방식

```
Request

↓

Chunk

↓

Client

↓

Chunk

↓

Client

↓

Chunk

↓

Client
```

---

## Streaming Parsing

응답은

```java
BodyHandlers.ofInputStream()
```

으로 받아온다.

InputStream을 BufferedReader로 읽으며

```java
reader.readLine()
```

을 반복 수행한다.

각 줄은 JSON 형태이며,

```json
{
  "response": "안",
  "done": false
}
```

와 같은 형태로 전달된다.

response 값을 추출하여

```java
Consumer.accept(chunk)
```

를 호출한다.

---

# Consumer Callback

Consumer를 이용하여

Streaming Engine과 Socket Writer를 분리하였다.

```java
streamChat(message,
    chunk -> {

        writer.write(chunk);
        writer.flush();

});
```

Streaming Engine은

Chunk 생성만 담당하고,

ClientHandler는

Chunk를 어떻게 처리할지 결정한다.

이를 통해 두 클래스의 의존성을 줄일 수 있었다.

---

# Streaming 구조

기존 구현

```
AI

↓

String 생성

↓

Client
```

현재 구현

```
AI

↓

Chunk 생성

↓

Consumer

↓

Socket

↓

Client
```

응답을 모두 생성할 때까지 기다리지 않고,

생성되는 즉시 사용자에게 전달한다.

---

# Backpressure

Streaming 응답은

AI 생성 속도와

클라이언트 출력 속도가 서로 다를 수 있다.

예를 들어

```
AI

100 chunk/sec

Client

20 chunk/sec
```

이라면

메모리가 계속 증가할 수 있다.

이를 방지하기 위해

```java
ArrayBlockingQueue<>(100)
```

을 사용하였다.

```
AI

↓

BlockingQueue

↓

Client
```

Queue가 가득 차면

```java
queue.put(chunk);
```

에서 생산자가 대기하게 된다.

이를 통해

- 메모리 사용량 제한
- 응답 순서 보장
- Producer-Consumer 분리

를 구현하였다.

---

# 사용 기술

- Java 21
- ServerSocket
- Socket
- HttpClient
- Virtual Thread
- ExecutorService
- BlockingQueue
- Consumer
- Streaming Response
- Ollama API

---

# 배운 점

이번 미션을 통해

- Streaming API 처리 방법
- Java HttpClient InputStream 사용법
- Virtual Thread 기반 AI 서버 구조
- Consumer Callback
- Producer-Consumer Pattern
- BlockingQueue 기반 Backpressure

를 직접 구현하며 학습하였다.

또한

AI Gateway Server는

CPU보다

네트워크 I/O가 훨씬 중요한 구조라는 점을 이해할 수 있었다.

---

# 개선 사항

- ExecutorService 기반 Virtual Thread 관리 개선
- Backpressure Strategy 분리
- Exponential Backoff 적용
- Timeout 기반 Queue 제어
- NIO Selector 기반 논블로킹 서버
- Reactive Streams(Flow API) 적용
- 다중 클라이언트 부하 테스트
- VisualVM 성능 분석
