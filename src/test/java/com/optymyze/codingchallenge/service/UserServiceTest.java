package com.optymyze.codingchallenge.service;

import com.optymyze.codingchallenge.model.User;
import com.optymyze.codingchallenge.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.*;

@ExtendWith(SpringExtension.class)
@SpringBootTest
public class UserServiceTest {
    @Autowired
    private UserService userService;

    @MockBean
    private UserRepository userRepository;

    @Test
    public void testAddUser() {
        User user = new User();
        user.setFirstName("Tayyaba");
        user.setSurName("Ambreen");
        user.setPosition("Consultant");
        user.setGitHubProfileUrl("https://github.com/tayyaba-ambreen");

        when(userRepository.save(user)).thenReturn(user);

        User createdUser = userService.addUser(user);

        assertNotNull(createdUser);
        assertEquals("Tayyaba", createdUser.getFirstName());
    }

    @Test
    public void testUpdateUser() {
        Long userId = 1L;
        User existingUser = new User();
        existingUser.setId(userId);
        existingUser.setFirstName("Tayyaba");
        existingUser.setSurName("Ambreen");
        existingUser.setPosition("Consultant");
        existingUser.setGitHubProfileUrl("https://github.com/tayyaba-ambreen");

        User updatedUser = new User();
        updatedUser.setId(userId);
        updatedUser.setFirstName("Sarah");
        updatedUser.setSurName("Ambreen");
        updatedUser.setPosition("Consultant");
        updatedUser.setGitHubProfileUrl("https://github.com/tayyaba-ambreen");

        when(userRepository.findById(userId)).thenReturn(Optional.of(existingUser));
        when(userRepository.save(updatedUser)).thenReturn(updatedUser);

        User result = userService.updateUser(updatedUser);

        assertNotNull(result);
        assertEquals("Sarah", result.getFirstName());
    }

    @Test
    public void testGetUserById() {
        Long userId = 2L;
        User user = new User();
        user.setId(userId);
        user.setFirstName("Tayyaba");
        user.setSurName("Ambreen");
        user.setPosition("Consultant");
        user.setGitHubProfileUrl("https://github.com/tayyaba-ambreen");

        when(userRepository.findById(userId)).thenReturn(Optional.of(user));

        User foundUser = userService.getUser(userId).get();

        assertNotNull(foundUser);
        assertEquals("Tayyaba", foundUser.getFirstName());
    }

    @Test
    public void testGetAllUsers() {
        User user = new User();
        user.setFirstName("Tayyaba");
        user.setSurName("Ambreen");
        user.setPosition("Consultant");
        user.setGitHubProfileUrl("https://github.com/tayyaba-ambreen");

        User user1 = new User();
        user1.setFirstName("Sarah");
        user1.setSurName("Adnan");
        user1.setPosition("Senior Consultant");
        user1.setGitHubProfileUrl("https://github.com/sarah-adnan");

        List<User> users = List.of(user, user1);
        when(userRepository.findAll()).thenReturn(users);
        when(userRepository.saveAll(users)).thenReturn(users);

        List<User> result = userService.getAllUsers();

        assertNotNull(result);
        assertEquals("Sarah", result.get(1).getFirstName());
    }

    @Test
    public void testDeleteUser() {
        Long userId = 3L;
        userService.deleteUser(userId);
        verify(userRepository, times(1)).deleteById(userId);
    }
}
