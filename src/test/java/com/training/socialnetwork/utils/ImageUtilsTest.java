package com.training.socialnetwork.utils;

import static org.junit.jupiter.api.Assertions.assertNotNull;

import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.springframework.mock.web.MockMultipartFile;

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
}
