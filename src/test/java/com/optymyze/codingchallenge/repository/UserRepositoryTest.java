package com.optymyze.codingchallenge.repository;

import com.optymyze.codingchallenge.model.User;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@ExtendWith(SpringExtension.class)
public class UserRepositoryTest {

    @Autowired
    private UserRepository userRepository;

    @Test
    public void testSaveUser() {
        User user = new User();
        user.setFirstName("Tayyaba");
        user.setSurName("Ambreen");
        user.setPosition("Consultant");
        user.setGitHubProfileUrl("https://github.com/tayyaba-ambreen");

        userRepository.save(user);

        assertNotNull(user.getId());

        User savedUser = userRepository.findById(user.getId()).orElse(null);
        assertNotNull(savedUser);
        assertEquals("Tayyaba", savedUser.getFirstName());
    }

    @Test
    public void testFindUserById() {
        User user = new User();
        user.setFirstName("Tayyaba");
        user.setSurName("Ambreen");
        user.setPosition("Consultant");
        user.setGitHubProfileUrl("https://github.com/tayyaba-ambreen");

        userRepository.save(user);

        User foundUser = userRepository.findById(user.getId()).orElse(null);

        assertNotNull(foundUser);
        assertEquals("Tayyaba", foundUser.getFirstName());
    }

    @Test
    public void testFindAllUsers() {
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

        userRepository.saveAll(List.of(user,user1));

        List<User> foundUsers = userRepository.findAll();

        assertNotNull(foundUsers);
        assertEquals("Tayyaba", foundUsers.get(0).getFirstName());
    }

    @Test
    public void testDeleteUser() {
        User user = new User();
        user.setFirstName("Tayyaba");
        user.setSurName("Ambreen");
        user.setPosition("Consultant");
        user.setGitHubProfileUrl("https://github.com/tayyaba-ambreen");

        userRepository.save(user);
        Long userId = user.getId();

        userRepository.deleteById(userId);

        User deletedUser = userRepository.findById(userId).orElse(null);
        assertNull(deletedUser);

    }
}
