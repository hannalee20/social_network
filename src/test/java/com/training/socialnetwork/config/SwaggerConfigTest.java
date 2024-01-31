package com.training.socialnetwork.config;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import org.junit.jupiter.api.Test;

import io.swagger.v3.oas.models.OpenAPI;

public class SwaggerConfigTest {

	@Test
    void testOpenAPIConfiguration() {
		SwaggerConfig swaggerConfig = new SwaggerConfig();
        OpenAPI openAPI = swaggerConfig.openAPI();

        assertNotNull(openAPI);
        assertEquals(1, openAPI.getSecurity().size());
    }
}
