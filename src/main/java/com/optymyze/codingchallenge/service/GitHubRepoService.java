package com.optymyze.codingchallenge.service;

import com.optymyze.codingchallenge.client.RestClient;
import com.optymyze.codingchallenge.constants.URI;
import com.optymyze.codingchallenge.dto.GitHubRepoDto;
import com.optymyze.codingchallenge.exception.GitProfileNotFoundException;
import com.optymyze.codingchallenge.exception.ResourceNotFoundException;
import com.optymyze.codingchallenge.model.User;
import com.optymyze.codingchallenge.util.StringUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Slf4j
@Service
public class GitHubRepoService {

    @Autowired
    private RestClient restClient;

    @Autowired
    UserService userService;

    @Value("${github.oauth.client-id}")
    private String clientId;

    @Value("${github.oauth.client-secret}")
    private String clientSecret;

    public ResponseEntity<List<GitHubRepoDto>> getUserGitHubRepositories(Long userId) {

        Optional<User> user = userService.getUser(userId);
        return user.map(u ->{
            String userRepoUrl = u.getGitHubProfileUrl();
            ResponseEntity responseEntity = null;
            if (!StringUtil.isEmpty(userRepoUrl)) {
                String apiUrl = userRepoUrl + URI.REPOS;
                HttpHeaders headers = new HttpHeaders();
                headers.setBasicAuth(clientId, clientSecret);
                HttpEntity<String> entity = new HttpEntity<>(headers);
                responseEntity = restClient.callApi(apiUrl, HttpMethod.GET, entity, new ParameterizedTypeReference<List<GitHubRepoDto>>() {
                });
            } else {
                throw new GitProfileNotFoundException(userId);
            }
            return responseEntity;
        }).orElseThrow(() -> new ResourceNotFoundException(userId));
    }
}
