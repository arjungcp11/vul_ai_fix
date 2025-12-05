package com.example.vulfixed.ai;

public interface AIClient {
    /**
     * Suggest a fixed version of the given Java source.
     * Implementations may call external APIs (OpenAI, local LLM) or return a mock annotated result.
     *
     * @param original original source code
     * @param filename path of the file (for context)
     * @return fixed source
     */
    String suggestFix(String original, String filename) throws Exception;
}
