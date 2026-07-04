# Java Backend Study

---

# 프로젝트 목표

- Java의 메모리 구조와 성능 이해
- AI 서비스에서 사용하는 벡터 연산 이해
- Platform Thread와 Virtual Thread의 차이 분석
- Pure Java 기반 AI Gateway Server 구현
- AI Streaming 및 Backpressure 구조 이해
- 실무에서 사용하는 AI 서버 아키텍처 학습

---

# 프로젝트 구성

| Mission | 내용 | 핵심 기술 |
|----------|------|-----------|
| Mission1 | RAG를 위한 Cosine Similarity 고속 연산 |  |
| Mission2 | Platform Thread vs Virtual Thread 성능 비교 | Virtual Thread, I/O Blocking, Profiling |
| Mission3 | Pure Java AI Streaming Gateway Server | HttpClient, Socket, Streaming, SSE, Backpressure |

---

# 기술 스택

## Language

- Java 21

## Networking

- ServerSocket
- Socket
- HttpClient

## Concurrency

- Platform Thread
- Virtual Thread
- BlockingQueue

## AI

- Ollama API
- Streaming Response
- Server-Sent Events(SSE)

## Profiling

- VisualVM

---

# Mission 소개

## Mission1

**RAG를 위한 Cosine Similarity 고속 연산**

 Cosine Similarity 구현

학습 내용

- Cosine Similarity
- VisualVM Profiling

---

## Mission2

**Platform Thread vs Virtual Thread 비교**

AI API처럼 I/O Blocking이 많은 환경에서 Platform Thread와 Virtual Thread의 성능 차이를 비교

학습 내용

- Platform Thread
- Virtual Thread
- Context Switching
- AI I/O Blocking
- Thread Profiling

---

## Mission3

**Pure Java Streaming AI Gateway Server**

Spring을 사용하지 않고 Java의 HttpClient와 Socket만으로 AI Streaming Gateway를 구현

학습 내용

- HttpClient
- Streaming Response
- SSE
- Consumer Callback
- Virtual Thread
- Backpressure
- BlockingQueue

---

# 학습 내용

- Java Memory Structure
- Heap Memory
- GC
- Primitive / Wrapper
- Cosine Similarity
- Virtual Thread
- Socket Programming
- Java HttpClient
- Streaming Response
- Server-Sent Events (SSE)
- Producer-Consumer Pattern
- BlockingQueue
- Backpressure

---

# 향후 개선 사항

- ExecutorService 기반 Virtual Thread 관리
- Backpressure 전략 분리
- Retry 정책 및 Exponential Backoff 적용
- Flow API(Reactive Streams) 적용
- Java NIO Selector 기반 논블로킹 서버 구현
- 부하 테스트 및 성능 분석

---

# 개발 환경

| 항목 | 버전 |
|------|------|
| Java | 21 |
| Build Tool | Gradle |
| IDE | IntelliJ IDEA |
| AI Model | Ollama (Gemma) |

---

# 참고

- Mission1 → `mission1/README.md`
- Mission2 → `mission2/README.md`
- Mission3 → `mission3/README.md`