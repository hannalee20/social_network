package com.training.socialnetwork.utils;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;

import com.training.socialnetwork.util.image.ImageUtils;

public class ImageUtilsTest {

	@InjectMocks
    private ImageUtils imageUtils;

    @Test
    void testSaveImage() throws Exception {
        imageUtils = new ImageUtils();
        MockMultipartFile mockFile = new MockMultipartFile("test-image.jpg", "test-image.jpg", "image/jpeg", "test data".getBytes());

        String result = imageUtils.saveImage(mockFile);
        assertNotNull(result);
    }
    
    @Test
    public void isValidTest() {
    	imageUtils = new ImageUtils();
        MultipartFile multipartFile = new MockMultipartFile("file", "test.png", "image/png", new byte[0]);

        boolean isValid = imageUtils.isValid(multipartFile);

        assertTrue(isValid);
    }

    @Test
    public void isNotValidTest() {
    	imageUtils = new ImageUtils();
        MultipartFile multipartFile = new MockMultipartFile("file", "test.txt", "text/plain", new byte[0]);

        boolean isValid = imageUtils.isValid(multipartFile);

        assertFalse(isValid);
    }

}
