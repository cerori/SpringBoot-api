package com.example.demo.repository;

import com.example.demo.domain.User;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.Collections;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(SpringExtension.class)
@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class UserRepositoryTest {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Test
    void whenFindByEmail_thenReturnUser() {
        String email = "test@test.com";
        String name = "tester";

        // given
        userRepository.save(User.builder()
                .email(email)
                .name(name)
                .password(passwordEncoder.encode("1234"))
                .roles(Collections.singletonList("USER"))
                .build()
        );

        // when
        Optional<User> user = userRepository.findByEmail(email);

        // then
        assertNotNull(user);
    }

}