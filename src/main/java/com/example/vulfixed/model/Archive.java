package com.example.vulfixed.model;

import jakarta.persistence.*;
import java.time.Instant;
import java.util.*;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

@Entity
@Table(name = "archives")
public class Archive {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String gitUrl;
    private Instant createdAt;

    @Lob
    private byte[] pdfBytes;

    @Lob
    @Column(columnDefinition = "TEXT")
    private String fileDiffsJson;

    @Transient
    private List<FileDiff> fileDiffs;

    public Long getId() { return id; }
    public String getGitUrl() { return gitUrl; }
    public void setGitUrl(String gitUrl) { this.gitUrl = gitUrl; }
    public Instant getCreatedAt() { return createdAt; }
    public void setCreatedAt(Instant createdAt) { this.createdAt = createdAt; }
    public byte[] getPdfBytes() { return pdfBytes; }
    public void setPdfBytes(byte[] pdfBytes) { this.pdfBytes = pdfBytes; }

    public List<FileDiff> getFileDiffs() {
        if (fileDiffs == null && fileDiffsJson != null) {
            try {
                ObjectMapper om = new ObjectMapper();
                fileDiffs = om.readValue(fileDiffsJson, new TypeReference<List<FileDiff>>() {});
            } catch (Exception e) { fileDiffs = new ArrayList<>(); }
        }
        return fileDiffs;
    }

    public void setFileDiffs(List<FileDiff> fileDiffs) {
        this.fileDiffs = fileDiffs;
        try {
            ObjectMapper om = new ObjectMapper();
            this.fileDiffsJson = om.writeValueAsString(fileDiffs);
        } catch (Exception e) { this.fileDiffsJson = "[]"; }
    }

    public static class FileDiff {
        private String path;
        private String original;
        private String fixed;
        private String issuesSummary;

        public FileDiff() {}
        public FileDiff(String path, String original, String fixed) {
            this.path = path;
            this.original = original;
            this.fixed = fixed;
            this.issuesSummary = null;
        }

        public String getPath() { return path; }
        public void setPath(String path) { this.path = path; }
        public String getOriginal() { return original; }
        public void setOriginal(String original) { this.original = original; }
        public String getFixed() { return fixed; }
        public void setFixed(String fixed) { this.fixed = fixed; }
        public String getIssuesSummary() { return issuesSummary; }
        public void setIssuesSummary(String issuesSummary) { this.issuesSummary = issuesSummary; }
    }
}
