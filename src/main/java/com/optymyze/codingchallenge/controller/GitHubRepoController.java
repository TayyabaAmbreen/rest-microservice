package com.optymyze.codingchallenge.controller;

import com.optymyze.codingchallenge.constants.URI;
import com.optymyze.codingchallenge.dto.GitHubRepoDto;
import com.optymyze.codingchallenge.service.GitHubRepoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(URI.API + URI.VERSION + URI.USERS)
public class GitHubRepoController {

    @Autowired
    GitHubRepoService gitHubRepoService;

    @GetMapping(value = "/{userId}"+ URI.REPOSITORIES)
    public ResponseEntity<List<GitHubRepoDto>> getUserGitHubRepositories(@PathVariable Long userId) {
        ResponseEntity<List<GitHubRepoDto>> responseEntity = gitHubRepoService.getUserGitHubRepositories(userId);
        return new ResponseEntity<>(responseEntity.getBody(), HttpStatus.OK);
    }

}
