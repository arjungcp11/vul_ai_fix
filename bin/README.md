# Spring AI Vulnerability Fixer

This project is a starter Spring Boot application that:
- Accepts a Git repository URL (POST /api/fix)
- Clones the repo using JGit
- Scans `.java` files and sends each file content to an AI service to "fix vulnerabilities"
- Stores original/fixed code and a generated PDF archive in MySQL
- Provides an endpoint to download the generated PDF

**Notes**
- This repo contains a mock AI provider (provider=mock) which returns an annotated version of each file.
- You can configure an OpenAI-compatible provider by implementing `OpenAIClient` and setting properties in `application.properties`.
- Only `application.properties` is used (no YAML).

Build:
```
mvn clean package
```

Run:
```
java -jar target/spring-ai-vuln-fixer-0.0.1-SNAPSHOT.jar
```
