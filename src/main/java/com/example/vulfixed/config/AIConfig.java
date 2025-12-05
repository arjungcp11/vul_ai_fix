package com.example.vulfixed.config;

import com.example.vulfixed.ai.AIClient;
import com.example.vulfixed.ai.MockAIClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AIConfig {

    @Value("${app.ai.provider:mock}")
    private String provider;

    @Bean
    public AIClient aiClient(MockAIClient mockAIClient) {
        // For now only mock is wired. Extend here to return an OpenAIClient when configured.
        if ("mock".equalsIgnoreCase(provider)) {
            return mockAIClient;
        }
        // future: if openai, return new OpenAIClient(...)
        return mockAIClient;
    }
}
