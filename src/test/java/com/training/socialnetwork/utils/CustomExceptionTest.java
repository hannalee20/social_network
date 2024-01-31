package com.training.socialnetwork.utils;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;

import com.training.socialnetwork.util.exception.CustomException;

public class CustomExceptionTest {

	@Test
    void testCustomException() {
        HttpStatus expectedHttpStatus = HttpStatus.NOT_FOUND;
        String expectedMessage = "Not found";

        CustomException customException = new CustomException(expectedHttpStatus, expectedMessage);

        assertEquals(expectedHttpStatus, customException.getHttpStatus());
        assertEquals(expectedMessage, customException.getMessage());
    }
}
