package com.optymyze.codingchallenge.service;

import com.optymyze.codingchallenge.client.RestClient;
import com.optymyze.codingchallenge.dto.GitHubRepoDto;
import com.optymyze.codingchallenge.exception.GitProfileNotFoundException;
import com.optymyze.codingchallenge.exception.ResourceNotFoundException;
import com.optymyze.codingchallenge.model.User;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(SpringExtension.class)
@SpringBootTest
public class GitHubRepoServiceTest {
    @Autowired
    private GitHubRepoService gitHubRepoService;

    @MockBean
    private UserService userService;

    @MockBean
    private RestClient restClient;

    @Test
    public void testGetUserRepositoriesWhenUserDoesNotExist() {
        Long userId = 1L;
        assertThrows(ResourceNotFoundException.class, () -> {
            gitHubRepoService.getUserGitHubRepositories(userId);
        });
    }

    @Test
    public void testGetUserRepositoriesWhenUserGitHubProfileDoesNotExist() {
        Long userId = 1L;
        User user = new User(1L, "Tayyaba", "Ambreen", "Consultant", null);
        when(userService.getUser(userId)).thenReturn(Optional.of(user));
        assertThrows(GitProfileNotFoundException.class, () -> {
            gitHubRepoService.getUserGitHubRepositories(userId);
        });
    }

    @Test
    public void testGetUserRepositoriesWhenUserGitHubProfileExists() {
        Long userId = 1L;
        User user = new User(1L, "Tayyaba", "Ambreen", "Consultant", "https://api.github.com/users/tayyaba-ambreen");
        GitHubRepoDto gitHubRepoDto = new GitHubRepoDto("spring-cloud-config-server", "tayyaba-ambreen/spring-cloud-config-server", "Spring cloud config server", "https://api.github.com/repos/tayyaba-ambreen/spring-cloud-config-server", "https://api.github.com/repos/tayyaba-ambreen/spring-cloud-config-server/languages",
                "2022-11-10T16:27:07Z", "2022-10-20T05:55:12Z", "2022-10-20T05:55:12Z", "git://github.com/tayyaba-ambreen/spring-cloud-config-server.git", "https://github.com/tayyaba-ambreen/spring-cloud-config-server.git", "Java");
        List<GitHubRepoDto> gitHubRepoDtos = List.of(gitHubRepoDto);
        ResponseEntity<List<GitHubRepoDto>> serviceResponse = new ResponseEntity<>(gitHubRepoDtos, HttpStatus.OK);
        when(userService.getUser(userId)).thenReturn(Optional.of(user));
        when(restClient.callApi(any(String.class), any(HttpMethod.class), any(HttpEntity.class), any(ParameterizedTypeReference.class))).thenReturn(serviceResponse);
        ResponseEntity<List<GitHubRepoDto>> actualResponse = gitHubRepoService.getUserGitHubRepositories(userId);

        assertNotNull(actualResponse);
        assertEquals("spring-cloud-config-server", actualResponse.getBody().get(0).getName());
    }

}
