package com.training.socialnetwork.utils;

import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.springframework.beans.BeanUtils;

import com.training.socialnetwork.dto.request.user.UserUpdateRequestDto;
import com.training.socialnetwork.entity.User;
import com.training.socialnetwork.util.mapper.ObjectMapperUtils;

public class ObjectMapperUtilsTest {

	@InjectMocks
    private ObjectMapperUtils objectMapperUtils;
	
	@Test
    public void copyPropertiesTest() {
		ObjectMapperUtils objectMapper = new ObjectMapperUtils();
        UserUpdateRequestDto userUpdateDto = new UserUpdateRequestDto();
        User userToUpdate = new User();
        
        objectMapper.copyProperties(userUpdateDto, userToUpdate);
        
        BeanUtils.copyProperties(userUpdateDto, userToUpdate, objectMapper.getNullPropertyNames(userUpdateDto));
    }

    @Test
    public void getNullPropertyNamesTest() {
    	ObjectMapperUtils objectMapper = new ObjectMapperUtils();
    	UserUpdateRequestDto userUpdateDto = new UserUpdateRequestDto();
        
        objectMapper.getNullPropertyNames(userUpdateDto);
    }
    
    @Test
    public void testGetDefaulterWithEmptyString() {
        objectMapperUtils = new ObjectMapperUtils();
        User user = new User();
        user.setUsername("");
        user.setUserId(0);
        
        objectMapperUtils.getDefaulter(user);
    }
}
