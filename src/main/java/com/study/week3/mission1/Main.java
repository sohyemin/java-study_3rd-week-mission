package com.study.week3.mission1;

import com.study.week3.mission1.model.VectorDocument;
import com.study.week3.mission1.search.SearchResult;
import com.study.week3.mission1.search.TopKSearcher;
import com.study.week3.mission1.similarity.CosineSimilarity;

import java.util.List;

public class Main {
    public static void main(String[] args) {

        CosineSimilarity cosineSimilarity = new CosineSimilarity();
        TopKSearcher searcher = new TopKSearcher(cosineSimilarity);


        float[] query = {1f, 0f};

        List<VectorDocument> documents = List.of(
                new VectorDocument(1, new float[]{1f, 0f}),
                new VectorDocument(2, new float[]{0f, 1f}),
                new VectorDocument(3, new float[]{-1f, 0f}),
                new VectorDocument(4, new float[]{1f, 1f}),
                new VectorDocument(5, new float[]{2f, 0f})
        );

        searcher.search(query, documents, 5);

    }
}
