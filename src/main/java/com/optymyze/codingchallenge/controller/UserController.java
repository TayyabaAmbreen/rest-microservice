package com.optymyze.codingchallenge.controller;

import com.optymyze.codingchallenge.constants.URI;
import com.optymyze.codingchallenge.dto.UserDto;
import com.optymyze.codingchallenge.exception.ResourceNotFoundException;
import com.optymyze.codingchallenge.mapper.UserMapper;
import com.optymyze.codingchallenge.model.User;
import com.optymyze.codingchallenge.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping(URI.API + URI.VERSION + URI.USERS)
public class UserController {

    @Autowired
    private UserMapper userMapper;

    @Autowired
    UserService userService;

    @PostMapping
    public ResponseEntity<UserDto> addUser(@Valid @RequestBody UserDto userDto) {
        User user = userMapper.toUser(userDto);
        userDto = userMapper.toUserDto(userService.addUser(user));
        return new ResponseEntity<>(userDto, HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<List<UserDto>> getAllUsers() {
        return new ResponseEntity<>(userMapper.toUserDtos(userService.getAllUsers()), HttpStatus.OK);
    }

    @GetMapping("/{userId}")
    public ResponseEntity<UserDto> getUser(@PathVariable Long userId) {
        return userService.getUser(userId)
                .map(user -> {
                    UserDto userDto = userMapper.toUserDto(user);
                    return new ResponseEntity(userDto, HttpStatus.OK);
                }).orElseThrow(() -> new ResourceNotFoundException(userId));
    }

    @PutMapping("/{userId}")
    public ResponseEntity<UserDto> updateUser(@PathVariable Long userId, @Valid @RequestBody UserDto userDto) {
        return userService.getUser(userId)
                .map(savedUser -> {
                    savedUser.setFirstName(userDto.getFirstName());
                    savedUser.setSurName(userDto.getSurName());
                    savedUser.setPosition(userDto.getPosition());
                    savedUser.setGitHubProfileUrl(userDto.getGitHubProfileUrl());

                    User updatedUser = userService.updateUser(savedUser);
                    UserDto resp = userMapper.toUserDto(updatedUser);
                    return new ResponseEntity(resp, HttpStatus.OK);
                }).orElseThrow(() -> new ResourceNotFoundException(userId));
    }

    @DeleteMapping("/{userId}")
    public ResponseEntity deleteUser(@PathVariable Long userId) {
        return userService.getUser(userId)
                .map(user -> {
                    userService.deleteUser(userId);
                    return new ResponseEntity(HttpStatus.OK);
                }).orElseThrow(() -> new ResourceNotFoundException(userId));
    }
}
