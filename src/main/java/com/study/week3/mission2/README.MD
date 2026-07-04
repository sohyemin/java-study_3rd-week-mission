# Mission 2 - Platform Thread vs Virtual Thread의 AI I/O Blocking 효율 비교

> AI API 호출과 같은 I/O Bound 환경에서 Platform Thread와 Virtual Thread의 성능을 비교하고, AI 서버에서 Virtual Thread가 사용되는 이유를 분석한다.

---

# 프로젝트 목표

AI 서비스는 대부분의 시간을 **네트워크 통신(I/O)** 에 사용한다.

사용자가 질문을 보내면 AI 모델이 답변을 생성하는 동안 서버는 CPU 연산보다 **외부 API의 응답을 기다리는 시간이 훨씬 길다.**

이번 미션에서는 동일한 AI 요청을 Platform Thread와 Virtual Thread에서 각각 실행하여 실행 시간과 스레드 사용량을 비교하였다.

---

# 학습 목표

- Platform Thread와 Virtual Thread의 차이 이해
- I/O Bound 작업의 특성 이해
- AI API 호출 구조 이해
- Java Virtual Thread 사용법 학습
- VisualVM을 이용한 Thread 관찰

---

# AI API 호출 구조

AI API 호출은 대부분 다음과 같은 과정을 거친다.

```text
사용자 요청

↓

HTTP 요청

↓

AI 모델 처리

↓

응답 생성

↓

HTTP 응답
```

이 과정에서 서버는 대부분 **응답을 기다리는 상태(Blocking)** 가 된다.

즉,

```
CPU 연산 <<<<<< I/O 대기
```

가 되는 작업이다.

---

# Platform Thread

Platform Thread는 운영체제(OS)의 Thread와 1:1로 연결된다.

```
Java Thread

↓

OS Thread
```

Thread 하나가 Blocking 되면 해당 OS Thread도 함께 대기하게 된다.

동시 요청이 많아질수록

- Thread 생성 비용
- 메모리 사용량
- Context Switching

비용이 증가한다.

---

# Virtual Thread

Virtual Thread는 Java Runtime이 관리하는 경량 Thread이다.

```
Virtual Thread

↓

Java Scheduler

↓

Platform Thread(Carrier Thread)
```

I/O 대기 상태에서는 Carrier Thread를 반환하고, 다른 Virtual Thread가 해당 Thread를 사용할 수 있다.

따라서 많은 수의 I/O 작업을 적은 비용으로 처리할 수 있다.

---

# 구현 내용

동일한 AI 요청을

- Platform Thread
- Virtual Thread

각각으로 실행하였다.

실험 항목

- 실행 시간
- Thread 사용량

---

# 구현 구조

## Platform Thread

```java
Executors.newFixedThreadPool(100)
```

100개의 Platform Thread를 생성하여 Thread Pool을 구성한다.

제출된 작업은 생성된 Thread가 순차적으로 처리하며, Thread 수는 최대 100개로 제한된다.

---

## Virtual Thread

```java
Executors.newThreadPerTaskExecutor(factory)
```

`ThreadFactory`를 이용하여 Virtual Thread를 생성하도록 설정하고, `newThreadPerTaskExecutor()`를 통해 **제출되는 작업(Task)마다 새로운 Virtual Thread를 생성하여 실행**하였다.

---

# 실행 결과

## 실행 시간

| 방식 | 실행 시간 |
|------|-----------:|
| Platform Thread | 약 200,897 ms |
| Virtual Thread | 약 2,120 ms |

Virtual Thread가 약 **95배 빠른 실행 시간**을 보였다.

---

## Thread 관찰

VisualVM을 통해 실행 중인 Thread를 확인하였다.

| 방식 | 관찰 Thread 수 |
|------|---------------:|
| Platform Thread | 약 100개 이상 |
| Virtual Thread | 약 8~13개 |

Virtual Thread는 많은 작업을 적은 수의 Carrier Thread로 처리하는 것을 확인하였다.

---

# 결과 분석

## Platform Thread

```
Client

↓

Platform Thread

↓

HTTP 요청

↓

응답 대기
```

응답을 기다리는 동안에도 OS Thread를 계속 점유한다.

동시 요청이 많아질수록 Thread 생성 비용과 Context Switching 비용이 증가한다.

---

## Virtual Thread

```
Client

↓

Virtual Thread

↓

HTTP 요청

↓

응답 대기

↓

Carrier Thread 반환
```

I/O 대기 중에는 Carrier Thread를 다른 Virtual Thread가 사용할 수 있다.

따라서 대량의 동시 요청에서도 효율적으로 처리할 수 있다.

---

# 왜 AI 서버에 적합한가?

AI Gateway Server는 대부분 다음과 같은 작업을 수행한다.

```
Client

↓

HTTP 요청

↓

AI API

↓

응답 대기
```

CPU 연산보다 네트워크 대기 시간이 훨씬 길기 때문에

Virtual Thread의 장점이 가장 크게 나타난다.

---

# 사용 기술

- Java 21
- Platform Thread
- Virtual Thread
- HttpClient
- VisualVM

---

# 배운 점

이번 실험을 통해

- Virtual Thread의 동작 원리
- I/O Blocking 환경에서의 장점
- Carrier Thread의 역할
- AI API 호출에서 Virtual Thread가 적합한 이유

를 이해할 수 있었다.

또한 Thread의 성능은 단순한 연산 속도가 아니라

- Blocking 시간
- Context Switching
- Thread 생성 비용

등 다양한 요소에 의해 결정된다는 점을 확인하였다.

---

# 개선 사항

- Context Switching 횟수 측정
- 대량 동시 요청 부하 테스트

---

# 결론

이번 미션에서는 Platform Thread와 Virtual Thread를 이용하여 동일한 AI 요청을 수행하고 성능을 비교하였다.

실험 결과 Virtual Thread는 I/O 대기 시간이 긴 AI API 호출 환경에서 Platform Thread보다 훨씬 효율적으로 동작하였다.