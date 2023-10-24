package com.optymyze.codingchallenge.controller;

import com.optymyze.codingchallenge.constants.URI;
import com.optymyze.codingchallenge.dto.GitHubRepoDto;
import com.optymyze.codingchallenge.exception.GitProfileNotFoundException;
import com.optymyze.codingchallenge.exception.ResourceNotFoundException;
import com.optymyze.codingchallenge.model.User;
import com.optymyze.codingchallenge.service.GitHubRepoService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(SpringExtension.class)
@WebMvcTest(GitHubRepoController.class)
public class GitHubRepoControllerTest {

    @MockBean
    GitHubRepoService gitHubRepoService;

    @Autowired
    MockMvc mockMvc;

    @Test
    public void testGetShouldReturn404NotFoundWhenGetByInvalidUserId() throws Exception {
        Long userId = 1L;
        Mockito.doThrow(ResourceNotFoundException.class).when(gitHubRepoService).getUserGitHubRepositories(userId);
        mockMvc.perform(get(URI.API + URI.VERSION + URI.USERS + "/{userId}" + URI.REPOSITORIES, userId))
                .andExpect(status().isNotFound());
    }

    @Test
    public void testGetShouldReturn404NotFoundWhenGetByUserIdWithNoGitHubProfile() throws Exception {
        Long userId = 1L;
        User user = new User(userId, "Tayyaba", "Ambreen", "Consultant", null);
        Mockito.doThrow(GitProfileNotFoundException.class).when(gitHubRepoService).getUserGitHubRepositories(userId);
        mockMvc.perform(get(URI.API + URI.VERSION + URI.USERS + "/{userId}" + URI.REPOSITORIES, userId))
                .andExpect(status().isNotFound());
    }

    @Test
    public void testGetShouldReturn200OKWhenGetByValidUserId() throws Exception {
        Long userId = 1L;
        User user = new User(userId, "Tayyaba", "Ambreen", "Consultant", "https://github.com/tayyaba-ambreen");
        GitHubRepoDto gitHubRepoDto = new GitHubRepoDto("spring-cloud-config-server", "tayyaba-ambreen/spring-cloud-config-server", "Spring cloud config server", "https://api.github.com/repos/tayyaba-ambreen/spring-cloud-config-server", "https://api.github.com/repos/tayyaba-ambreen/spring-cloud-config-server/languages",
                "2022-11-10T16:27:07Z", "2022-10-20T05:55:12Z", "2022-10-20T05:55:12Z", "git://github.com/tayyaba-ambreen/spring-cloud-config-server.git", "https://github.com/tayyaba-ambreen/spring-cloud-config-server.git", "Java");
        List<GitHubRepoDto> gitHubRepoDtos = List.of(gitHubRepoDto);
        ResponseEntity<List<GitHubRepoDto>> serviceResponse = new ResponseEntity<>(gitHubRepoDtos, HttpStatus.OK);
        when(gitHubRepoService.getUserGitHubRepositories(userId)).thenReturn(serviceResponse);

        mockMvc.perform(get(URI.API + URI.VERSION + URI.USERS + "/{userId}" + URI.REPOSITORIES, userId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(gitHubRepoDtos.size()))
                .andExpect(jsonPath("$[0].name").value(gitHubRepoDto.getName()))
                .andExpect(jsonPath("$[0].full_name").value(gitHubRepoDto.getFullName()));

    }

}
