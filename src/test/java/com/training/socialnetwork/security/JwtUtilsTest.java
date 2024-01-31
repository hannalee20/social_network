package com.training.socialnetwork.security;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.when;

import javax.servlet.http.HttpServletRequest;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

public class JwtUtilsTest {

	@InjectMocks
    private JwtUtils jwtUtils;

    @Mock
    private HttpServletRequest request;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testGetJwt() {
        when(request.getHeader("Authorization")).thenReturn("Bearer your_jwt_token_string");

        String jwt = jwtUtils.getJwt(request);

        assertNotNull(jwt);
        assertEquals("your_jwt_token_string", jwt);
    }
}
