package com.study.week3.mission1.search;

public class SearchResult {
    private final int documentId;
    private final float score;

    public SearchResult(int documentId, float score) {
        this.documentId = documentId;
        this.score = score;
    }

    public float getScore() {
        return score;
    }

    public int getDocumentId() {
        return documentId;
    }
}
