package com.mindhub.todolist.repositories;

import com.mindhub.todolist.models.UserEntity;
import com.mindhub.todolist.models.enums.UserRoles;
import com.mindhub.todolist.respositories.UserEntityRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
public class UserRepositoryTest {

    @TestConfiguration
    static class TestConfig {
        @Bean
        public PasswordEncoder passwordEncoder() {
            return new BCryptPasswordEncoder();
        }
    }

    @Autowired
    private UserEntityRepository userEntityRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;

    @Test
    public void testFindByEmailSucess() {
        UserEntity user1 = new UserEntity("john",passwordEncoder.encode("020202020"),"demotest@demo.com", UserRoles.ADMIN);
        userEntityRepository.save(user1);
        UserEntity foundUser = userEntityRepository.findByEmail("demotest@demo.com").orElse(null);
        assertNotNull(foundUser);
        assertEquals("john", foundUser.getUsername());
    }

    @Test
    public void testFindByEmailFail() {
        UserEntity foundUser = userEntityRepository.findByEmail("demotestfail@demo.com").orElse(null);
        assertNull(foundUser);
    }

}
