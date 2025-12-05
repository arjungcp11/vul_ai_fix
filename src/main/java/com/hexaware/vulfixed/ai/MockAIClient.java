package com.hexaware.vulfixed.ai;

import org.springframework.stereotype.Component;

@Component
public class MockAIClient implements AIClient {

    @Override
    public String suggestFix(String original, String filename) {
        // Simple mocked "fix" - in production, call real AI provider
        String header = "/* AI-MOCK-FIX for " + filename + " - review suggested changes below */\n";
        String suggestion = "\n// SUGGESTIONS:\n// - Review SQL injection points, use prepared statements\n// - Validate inputs\n";
        return header + suggestion + original;
    }
}
