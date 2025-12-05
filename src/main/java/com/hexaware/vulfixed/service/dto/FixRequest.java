package com.hexaware.vulfixed.service.dto;

public class FixRequest {
    private String gitUrl;
    private String branch;

    public String getGitUrl() { return gitUrl; }
    public void setGitUrl(String gitUrl) { this.gitUrl = gitUrl; }

    public String getBranch() { return branch; }
    public void setBranch(String branch) { this.branch = branch; }
}
