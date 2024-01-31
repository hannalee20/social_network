package com.training.socialnetwork.security;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import com.training.socialnetwork.service.impl.CustomUserDetailService;

@ExtendWith(MockitoExtension.class)
@WebMvcTest(WebSecurityConfig.class)
public class WebSecurityConfigTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
	private WebApplicationContext webApplicationContext;

    @MockBean
    private CustomUserDetailService userDetailService;

    @MockBean
    private BCryptPasswordEncoder passwordEncoder;

    @MockBean
    private AuthenticationEntryPoint unauthorizedHandler;

    @MockBean
    private AuthenticationManager authenticationManager;

    @MockBean
    private JwtUtils jwtUtils;
    
    @BeforeEach
	void setUp() throws Exception {
		mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
	}
    
    @Test
    public void testUnsecuredEndpoint() throws Exception {
        mockMvc.perform(get("/unsecured-endpoint"))
               .andExpect(status().isNotFound());
    }
}