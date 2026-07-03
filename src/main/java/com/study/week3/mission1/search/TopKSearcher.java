package com.study.week3.mission1.search;

import com.study.week3.mission1.similarity.CosineSimilarity;

import java.util.Comparator;
import java.util.PriorityQueue;

public class TopKSearcher {

    private final CosineSimilarity cosineSimilarity;
    public TopKSearcher(CosineSimilarity cosineSimilarity) {
        this.cosineSimilarity = cosineSimilarity;
    }

    public SearchResult[] search (float[] query, float[][] docs, int top){

        PriorityQueue<SearchResult> queue = new PriorityQueue<>(new Comparator<SearchResult>() {
            @Override
            public int compare(SearchResult o1, SearchResult o2) {
                return Integer.compare(o1.getScore(), o2.getScore()) ;
            }
        });


        for (int i = 0; i < docs.length; i++) {

            int compare = cosineSimilarity.compare(query, docs[i]);
            SearchResult result = new SearchResult(i, compare);

            if (query.length!=5)
                queue.add(result);
            else {
            }
        }


        return null;
    }
}
