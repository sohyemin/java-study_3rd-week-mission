package com.study.week3.mission1.search;

import com.study.week3.mission1.model.VectorDocument;
import com.study.week3.mission1.similarity.CosineSimilarity;

import java.util.ArrayList;
import java.util.List;
import java.util.PriorityQueue;

public class TopKSearcher {

    private final CosineSimilarity cosineSimilarity;
    public TopKSearcher(CosineSimilarity cosineSimilarity) {
        this.cosineSimilarity = cosineSimilarity;
    }

    public void search (float[] query, List<VectorDocument> docs, int top) {

        PriorityQueue<SearchResult> queue =
                new PriorityQueue<>((a, b) -> Float.compare(a.getScore(), b.getScore()));

        for (VectorDocument doc : docs) {
            float compare = cosineSimilarity.compare(query, doc.getVector());
            SearchResult result = new SearchResult(doc.getId(), compare);

            queue.add(result);

            if (queue.size() > top) {
                queue.poll(); // 가장 낮은 점수 제거
            }
        }

        List<SearchResult> results = new ArrayList<>(queue);
        results.sort((a, b) -> Float.compare(b.getScore(), a.getScore()));

        for (SearchResult result : results) {
            System.out.println("documentId : " + result.getDocumentId()
                    + ", score : " + result.getScore());
        }
    }
}
