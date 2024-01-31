package com.training.socialnetwork.config;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;
import org.modelmapper.ModelMapper;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

public class AppConfigTest {

	@Test
    void testModelMapperBean() {
        AppConfig appConfig = new AppConfig();
        ModelMapper modelMapper = appConfig.modelMapper();
        assertNotNull(modelMapper);
     }

    @Test
    void testBCryptPasswordEncoderBean() {
        AppConfig appConfig = new AppConfig();
        BCryptPasswordEncoder passwordEncoder = appConfig.passwordEncoder();
        assertNotNull(passwordEncoder);
        String rawPassword = "123456";
        String encodedPassword = passwordEncoder.encode(rawPassword);
        assertTrue(passwordEncoder.matches(rawPassword, encodedPassword));
    }
}
