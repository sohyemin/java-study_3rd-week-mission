package com.study.week3.mission1.search;

public class SearchResult {
    private final int documentId;
    private final int score;

    public SearchResult(int documentId, int score) {
        this.documentId = documentId;
        this.score = score;
    }

    public int getScore() {
        return score;
    }
}
