package com.optymyze.codingchallenge.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class GitHubRepoDto implements Serializable {
    private static final long serialVersionUID = 1L;

    private String name;
    @JsonProperty("full_name")
    private String fullName;
    private String description;
    private String url;
    @JsonProperty("languages_url")
    private String languagesUrl;
    @JsonProperty("created_at")
    private String createdAt;
    @JsonProperty("updated_at")
    private String updatedAt;
    @JsonProperty("pushed_at")
    private String pushedAt;
    @JsonProperty("git_url")
    private String gitUrl;
    @JsonProperty("clone_url")
    private String cloneUrl;
    private String language;
}
