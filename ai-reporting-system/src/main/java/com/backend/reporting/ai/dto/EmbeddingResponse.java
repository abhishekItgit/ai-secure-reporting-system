package com.backend.reporting.ai.dto;

import java.util.List;

public class EmbeddingResponse {

    private List<EmbeddingData> data;

    public List<EmbeddingData> getData() {
        return data;
    }

    public static class EmbeddingData {
        private float[] embedding;

        public float[] getEmbedding() {
            return embedding;
        }
    }
}
