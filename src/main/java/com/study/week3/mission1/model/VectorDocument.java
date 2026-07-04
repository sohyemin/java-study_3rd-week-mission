package com.study.week3.mission1.model;

public class VectorDocument {
    private final int id;
    private final float[] vector;

    public VectorDocument(int id, float[] vector) {
        this.id = id;
        this.vector = vector;
    }

    public int getId() {
        return id;
    }

    public float[] getVector() {
        return vector;
    }

}
