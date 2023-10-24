package com.optymyze.codingchallenge.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.optymyze.codingchallenge.constants.URI;
import com.optymyze.codingchallenge.dto.UserDto;
import com.optymyze.codingchallenge.exception.ResourceNotFoundException;
import com.optymyze.codingchallenge.mapper.UserMapper;
import com.optymyze.codingchallenge.model.User;
import com.optymyze.codingchallenge.service.UserService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(SpringExtension.class)
@WebMvcTest(UserController.class)
public class UserControllerTest {

    @MockBean
    UserService userService;

    @Autowired
    MockMvc mockMvc;

    @MockBean
    UserMapper userMapper;

    @Autowired
    ObjectMapper objectMapper;

    @Test
    public void testAddShouldReturn400BadRequestWhenFirstNameIsNull() throws Exception {
        User user = new User(1L, null, "Ambreen", "Consultant", "https://github.com/tayyaba-ambreen");
        UserDto userDto = new UserDto(1L, null, "Ambreen", "Consultant", "https://github.com/tayyaba-ambreen");

        when(userMapper.toUserDto(user)).thenReturn(userDto);
        String requestBody = objectMapper.writeValueAsString(userDto);

        mockMvc.perform(post(URI.API + URI.VERSION + URI.USERS).contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void testAddShouldReturn400BadRequestWhenPositionIsNull() throws Exception {
        User user = new User(1L, "Tayyaba", "Ambreen", null, "https://github.com/tayyaba-ambreen");
        UserDto userDto = new UserDto(1L, "Tayyaba", "Ambreen", null, "https://github.com/tayyaba-ambreen");

        when(userMapper.toUserDto(user)).thenReturn(userDto);
        String requestBody = objectMapper.writeValueAsString(userDto);

        mockMvc.perform(post(URI.API + URI.VERSION + URI.USERS).contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void testAddShouldReturn201CreatedWhenValidUserIsAdded() throws Exception {
        User user = new User(1L, "Tayyaba", "Ambreen", "Consultant", "https://github.com/tayyaba-ambreen");
        UserDto userDto = new UserDto(1L, "Tayyaba", "Ambreen", "Consultant", "https://github.com/tayyaba-ambreen");

        when(userMapper.toUserDto(user)).thenReturn(userDto);
        String requestBody = objectMapper.writeValueAsString(userDto);

        mockMvc.perform(post(URI.API + URI.VERSION + URI.USERS).contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andExpect(status().isCreated());
    }

    @Test
    public void testGetShouldReturn404NotFoundWhenGetByInvalidUserId() throws Exception {
        Long userId = 1L;
        when(userService.getUser(userId)).thenReturn(Optional.empty());
        mockMvc.perform(get(URI.API + URI.VERSION + URI.USERS + "/{userId}", userId))
                .andExpect(status().isNotFound());
    }

    @Test
    public void testGetShouldReturn200OKWhenGetByValidUserId() throws Exception {
        Long userId = 1L;
        User user = new User(1L, "Tayyaba", "Ambreen", "Consultant", "https://github.com/tayyaba-ambreen");
        UserDto userDto = new UserDto(1L, "Tayyaba", "Ambreen", "Consultant", "https://github.com/tayyaba-ambreen");
        when(userService.getUser(userId)).thenReturn(Optional.of(user));
        when(userMapper.toUserDto(user)).thenReturn(userDto);
        mockMvc.perform(get(URI.API + URI.VERSION + URI.USERS + "/{userId}", userId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.firstName").value(user.getFirstName()))
                .andExpect(jsonPath("$.surName").value(user.getSurName()))
                .andExpect(jsonPath("$.position").value(user.getPosition()));

    }

    @Test
    public void testGetAllShouldReturn200OKWhenGetAllUsers() throws Exception {
        Long userId = 1L;
        User user = new User(1L, "Tayyaba", "Ambreen", "Consultant", "https://github.com/tayyaba-ambreen");
        User user1 = new User(2L, "Sarah", "Adnan", "Senior Consultant", "https://github.com/sarah-adnan");
        List<User> users = List.of(user, user1);
        UserDto userDto = new UserDto(1L, "Tayyaba", "Ambreen", "Consultant", "https://github.com/tayyaba-ambreen");
        UserDto userDto1 = new UserDto(2L, "Sarah", "Adnan", "Senior Consultant", "https://github.com/sarah-adnan");
        List<UserDto> userDtos = List.of(userDto, userDto1);
        when(userService.getAllUsers()).thenReturn(users);
        when(userMapper.toUserDtos(users)).thenReturn(userDtos);
        mockMvc.perform(get(URI.API + URI.VERSION + URI.USERS)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(userDtos.size()))
                .andExpect(jsonPath("$[0].firstName").value(userDto.getFirstName()))
                .andExpect(jsonPath("$[0].surName").value(userDto.getSurName()))
                .andExpect(jsonPath("$[0].position").value(userDto.getPosition()));

    }

    @Test
    public void testDeleteShouldReturn404NotFoundWhenDeleteByInvalidUserId() throws Exception {
        Long userId = 1L;
        Mockito.doThrow(ResourceNotFoundException.class).when(userService).deleteUser(userId);

        mockMvc.perform(delete(URI.API + URI.VERSION + URI.USERS + "/{userId}", userId))
                .andExpect(status().isNotFound());
    }

    @Test
    public void testDeleteShouldReturn200OKWhenDeleteByValidUserId() throws Exception {
        Long userId = 1L;
        User user = new User(1L, "Tayyaba", "Ambreen", "Consultant", "https://github.com/tayyaba-ambreen");
        when(userService.getUser(userId)).thenReturn(Optional.of(user));
        Mockito.doNothing().when(userService).deleteUser(userId);

        mockMvc.perform(delete(URI.API + URI.VERSION + URI.USERS + "/{userId}", userId))
                .andExpect(status().isOk());
    }

    @Test
    public void testUpdateShouldReturn404NotFoundWhenInvalidUserIsUpdated() throws Exception {
        Long userId = 1L;
        User savedUser = new User(1L, "Tayyaba", "Ambreen", "Junior Consultant", "https://github.com/tayyaba-ambreen");
        User updatedUser = new User(1L, "Tayyaba", "Ambreen", "Consultant", "https://github.com/tayyaba-ambreen");

        when(userService.getUser(userId)).thenReturn(Optional.empty());
        when(userService.updateUser(any(User.class))).thenReturn(updatedUser);
        mockMvc.perform(put(URI.API + URI.VERSION + URI.USERS + "/{userId}", userId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updatedUser)))
                .andExpect(status().isNotFound());
    }

    @Test
    public void testUpdateShouldReturn200OKWhenValidUserIsUpdated() throws Exception {
        Long userId = 1L;
        User savedUser = new User(1L, "Tayyaba", "Ambreen", "Junior Consultant", "https://github.com/tayyaba-ambreen");
        User updatedUser = new User(1L, "Tayyaba", "Ambreen", "Consultant", "https://github.com/tayyaba-ambreen");
        UserDto updatedUserDto = new UserDto(1L, "Tayyaba", "Ambreen", "Consultant", "https://github.com/tayyaba-ambreen");

        when(userService.getUser(userId)).thenReturn(Optional.of(savedUser));
        when(userService.updateUser(any(User.class))).thenReturn(updatedUser);
        when(userMapper.toUserDto(updatedUser)).thenReturn(updatedUserDto);
        mockMvc.perform(put(URI.API + URI.VERSION + URI.USERS + "/{userId}", userId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updatedUser)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.position").value(updatedUser.getPosition()));
    }
}
