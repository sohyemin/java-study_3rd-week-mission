package com.study.week3.mission3.factory;


import com.study.week3.mission3.engine.AIEngine;
import com.study.week3.mission3.engine.OllamaEngine;

public class AIEngineFactory {

    public static AIEngine create(String type){

        return new OllamaEngine();

    }
}
