package com.study.week3.mission1.search;

import java.util.Comparator;

public class SearchComparator implements Comparator<SearchResult> {
    @Override
    public int compare(SearchResult o1, SearchResult o2) {
        return Float.compare(o2.getScore(), o1.getScore()) ;
    }
}
