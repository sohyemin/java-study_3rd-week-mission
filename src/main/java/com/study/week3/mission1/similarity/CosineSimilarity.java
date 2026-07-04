package com.study.week3.mission1.similarity;

import com.study.week3.mission1.model.VectorDocument;

public class CosineSimilarity {
    public float compare(float[] vector1, float[] vector2){

        // 벡터 내적 계산
        float dot = 0;
        for (int i = 0; i < vector1.length; i++) {
            dot += vector1[i] * vector2[i];
        }

        // 벡터A 크기 계산
        float normA = 0;
        for (float value : vector1) {
            normA += value * value;
        }

        float normB = 0;
        for (float v : vector2) {
            normB += v * v;
        }

        if (normA == 0 || normB == 0)
            return 0;
        else {
            return (float) (dot/(Math.sqrt(normA)*Math.sqrt(normB)));
        }
    }


}
