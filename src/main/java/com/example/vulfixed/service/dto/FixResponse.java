package com.example.vulfixed.service.dto;

public class FixResponse {
    private Long archiveId;
    private String message;

    public FixResponse() {}
    public FixResponse(Long archiveId, String message) {
        this.archiveId = archiveId;
        this.message = message;
    }

    public Long getArchiveId() { return archiveId; }
    public void setArchiveId(Long archiveId) { this.archiveId = archiveId; }
    public String getMessage() { return message; }
    public void setMessage(String message) { this.message = message; }
}
