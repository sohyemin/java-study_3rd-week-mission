# Mission 1 - RAG를 위한 Cosine Similarity 구현

> RAG(Retrieval-Augmented Generation)의 검색 단계에서 사용되는 **Cosine Similarity**를 Java로 직접 구현하고, 벡터 유사도 계산 과정을 이해한다.

---

# 프로젝트 목표

LLM은 학습되지 않은 최신 정보나 내부 문서를 알지 못한다.

이를 해결하기 위해 등장한 기술이 **RAG(Retrieval-Augmented Generation)** 이며, 질문과 가장 유사한 문서를 검색한 후 LLM에게 함께 전달하여 답변을 생성한다.

이번 미션에서는 RAG의 검색 과정에서 가장 많이 사용되는 **Cosine Similarity** 알고리즘을 Java로 직접 구현하였다.

---

# RAG(Retrieval-Augmented Generation)란?

RAG는 질문과 가장 관련성이 높은 문서를 먼저 검색한 후, 검색 결과를 LLM에게 함께 전달하여 답변의 정확도를 높이는 기술이다.

검색 과정은 다음과 같다.

```text
사용자 질문

↓

Embedding 생성

↓

문서 Embedding

↓

Cosine Similarity 계산

↓

가장 유사한 문서 선택

↓

LLM Prompt 생성

↓

답변 생성
```

즉, Cosine Similarity는 **RAG의 검색 품질을 결정하는 핵심 알고리즘** 중 하나이다.

---

# Cosine Similarity란?

Cosine Similarity는 두 벡터 사이의 **방향 유사도(Direction Similarity)** 를 계산하는 알고리즘이다.

벡터의 크기(Magnitude)가 아니라 방향을 비교하기 때문에 자연어 처리와 AI 검색에서 널리 사용된다.

공식은 다음과 같다.

```
             A · B
--------------------------------
||A|| × ||B||
```

### 결과 범위

| 값 | 의미 |
|-----|------|
| 1 | 동일한 방향 |
| 0 | 서로 무관 |
| -1 | 반대 방향 |

---

# 알고리즘 구현

Cosine Similarity는 크게 세 단계로 계산된다.

## 1. Dot Product 계산

```java
dot += vectorA[i] * vectorB[i];
```

두 벡터의 같은 위치 값을 곱한 후 모두 더한다.

---

## 2. 각 벡터의 크기 계산

```java
normA += vectorA[i] * vectorA[i];
normB += vectorB[i] * vectorB[i];
```

각 벡터의 길이를 계산하기 위해 제곱합을 구한다.

최종적으로

```java
Math.sqrt(normA)
Math.sqrt(normB)
```

를 수행하여 벡터의 크기를 계산한다.

---

## 3. Cosine Similarity 계산

```java
return dot /
       (Math.sqrt(normA) * Math.sqrt(normB));
```

Dot Product를 두 벡터의 크기로 나누어 최종 유사도를 계산한다.

---

# 구현 과정

이번 미션에서는 다음 순서로 구현하였다.

1. 두 벡터 생성
2. Dot Product 계산
3. 벡터 크기 계산
4. Cosine Similarity 계산
5. 결과 출력

전체 연산은 하나의 반복문에서 수행하도록 구현하여 불필요한 반복을 최소화하였다.

---

# 실행 예시

예를 들어

```
Vector A

[1, 2, 3]

Vector B

[2, 4, 6]
```

이라면

```
Cosine Similarity

= 1
```

이 계산된다.

두 벡터가 동일한 방향을 가지기 때문이다.

반대로 방향이 전혀 다르면 0에 가까워지고, 반대 방향이라면 -1에 가까운 값을 갖는다.

---

# RAG에서의 활용

RAG에서는 사용자의 질문과 문서를 모두 **Embedding Vector**로 변환한다.

예를 들어

```text
질문

↓

[0.12, 0.55, 0.81 ...]

↓

문서1

↓

[0.11, 0.54, 0.82 ...]

↓

문서2

↓

[0.80, 0.20, 0.10 ...]

↓

Cosine Similarity 계산

↓

가장 높은 문서 선택
```

이 과정을 수천~수백만 번 반복하여 가장 유사한 문서를 검색한다.

---

# 구현 시 고려한 사항

이번 구현에서는 벡터 연산이 반복적으로 수행되는 특성을 고려하여

```java
double[]
```

기반으로 구현하였다.

Primitive 배열은 객체 생성 비용이 없고 연속된 메모리 공간을 사용하기 때문에 대량의 벡터 연산에 적합하다.

---

# 사용 기술

- Java 21
- Primitive Array
- Cosine Similarity
- Vector 연산

---

# 배운 점

이번 미션을 통해

- Cosine Similarity의 계산 원리
- Dot Product와 Vector Norm의 의미
- RAG에서 문서를 검색하는 과정
- Java로 벡터 연산을 구현하는 방법

을 이해할 수 있었다.

또한 AI 서비스에서 단순히 LLM을 사용하는 것뿐 아니라, **검색 알고리즘(Retrieval)** 역시 중요한 역할을 한다는 점을 확인할 수 있었다.

---

# 개선 사항

- 대용량 벡터 데이터 검색
- 다양한 유사도 알고리즘 비교
    - Euclidean Distance
    - Manhattan Distance
    - Cosine Similarity
- Parallel Stream을 이용한 병렬 계산
- FAISS, ChromaDB, pgvector 등 Vector Database와 연동