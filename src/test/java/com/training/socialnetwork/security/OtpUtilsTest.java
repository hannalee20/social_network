package com.training.socialnetwork.security;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.concurrent.ExecutionException;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.google.common.cache.LoadingCache;

public class OtpUtilsTest {

	@InjectMocks
    private OtpUtils otpUtils;

    @Mock
    private LoadingCache<String, Integer> otpCache;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testGenerateOtp() throws Exception {
        String key = "testKey";

        int generatedOtp = otpUtils.generateOtp(key);

        assertTrue(generatedOtp >= 100000 && generatedOtp <= 999999);
        verify(otpCache, times(1)).put(eq(key), anyInt());
    }

    @Test
    void testGetOtp() throws ExecutionException {
        String key = "testKey";

        when(otpCache.get(key)).thenReturn(123456);

        int retrievedOtp = otpUtils.getOtp(key);
        assertEquals(123456, retrievedOtp);
    }

    @Test
    void testClearOtp() {
        String key = "testKey";

        otpUtils.clearOtp(key);
        verify(otpCache, times(1)).invalidate(key);
    }

    @Test
    void testValidateOtp() throws ExecutionException {
        String key = "testKey";
        int otpFromCache = 123456;

        when(otpCache.get(key)).thenReturn(otpFromCache);

        boolean result = otpUtils.validateOtp(key, otpFromCache);
        assertTrue(result);
        verify(otpCache, times(1)).invalidate(key);
    }
}
