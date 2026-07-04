package com.study.week3.mission3.engine;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.util.Map;
import java.util.function.Consumer;

public class OllamaStreamingEngine implements StreamingAIEngine {

    private final HttpClient client = HttpClient.newHttpClient();
    private final ObjectMapper objectMapper = new ObjectMapper();

    private final String model = "gemma4";

    @Override
    public void streamChat(String message, Consumer<String> chunkoConsumer) {
        try {
            Map<String, Object> requestBody = Map.of(
                    "model", model,
                    "prompt", message,
                    "stream", true // full response 를 기다리는 형식이 아닌 생성되는 조각을 즉시 받아옴.
            );

            String jsonBody = objectMapper.writeValueAsString(requestBody);

            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create("http://localhost:11434/api/generate"))
                    .header("Content-Type", "application/json")
                    .POST(HttpRequest.BodyPublishers.ofString(jsonBody))
                    .build();

            HttpResponse<InputStream> response = client.send(
                    request,
                    HttpResponse.BodyHandlers.ofInputStream() //응답을 스트림으로 받아서 도착하는 데이터를 계속 읽어오게 함.
            );

            try (BufferedReader reader = new BufferedReader(
                    new InputStreamReader(response.body(), StandardCharsets.UTF_8)
            )){
                String line;

                while ((line = reader.readLine()) != null) {
                    String chunk = parseChunk(line);

                    if(chunk!=null && !chunk.isEmpty()){
                        chunkoConsumer.accept(chunk);
                    }
                }
            }
        } catch (Exception e){
            chunkoConsumer.accept("\n[Ollama 호출 중 오류 발생: " + e.getMessage() + "]");
        }
    }

    private String parseChunk(String line) throws Exception{
        if (line == null || line.isEmpty()){
            return null;
        }

        JsonNode root = objectMapper.readTree(line);

        if(root.has("error")){
            return "[Ollama 오류: " + root.get("error").asText() + "]";
        }

        if (root.has("response")) {
            return root.get("response").asText();
        }

        return null;
    }
}
